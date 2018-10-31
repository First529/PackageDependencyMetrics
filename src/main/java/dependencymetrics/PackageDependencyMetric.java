package dependencymetrics;

import java.io.File;
import java.util.*;

public class PackageDependencyMetric {

    private Map<String, PackageInfo> packageInfoMap;
    private Map<String, ClassInfo> allClassInfos;
    static String rootPackageName;

    public PackageDependencyMetric() {
        packageInfoMap = new TreeMap<>();
        allClassInfos = new TreeMap<>();
    }

    public Map<String,PackageInfo> getPackageInfoMap() {
        return packageInfoMap;
    }


    public void calculateMetrics(String directoryName, String rootPackageName) {
        this.rootPackageName = rootPackageName;
        calculateMetricsRecursive(directoryName);
        calculateAfferentCouplings();
    }

    private void calculateAfferentCouplings() {

        // read import
        // see which package it imports
        // count itself as using package
        Collection<ClassInfo> all = allClassInfos.values();
        for (ClassInfo classInfo : all) {
            Set<String> importList = classInfo.getPackagesImported();

            for (String importPackage : importList) {
                PackageInfo p = packageInfoMap.get(importPackage);
                if (p != null) {
                    p.incrementAfferentCouplings();
                } else {

                    // it might import inner classes
                    // check to make sure it is a class within this project
                    ClassInfo c = allClassInfos.get(importPackage);
                    int classNameIndex = getFirstCapitalIndex(importPackage);

                    if (c != null && classNameIndex >= 0) {
                        p = packageInfoMap.get(importPackage.substring(0,classNameIndex-1));
                        p.incrementAfferentCouplings();
                    }
                }
            }
        }
    }


    private void calculateMetricsRecursive(String directoryName) {
        File folder = new File(directoryName);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {

                if (notValidJavaFile(listOfFiles[i].getName()))
                    continue;

                ClassInfo classInfo = new ClassInfo(listOfFiles[i]);
                String packageName = classInfo.getPackageName();

                // not in any packages
                if (packageName == null) {
                    continue;
                }

                allClassInfos.put(classInfo.getPackageName() + "." + classInfo.getClassName(),
                        classInfo);

                // read package name
                // add package name if doesn't exist
                // count itself as within package
                if (packageInfoMap.get(packageName) == null) {
                    packageInfoMap.put(packageName, new PackageInfo(packageName));
                }
                PackageInfo packageInfo = packageInfoMap.get(packageName);
                packageInfo.incrementClass();
                packageInfo.addClassToPackage(classInfo);

                // if it is an abstract class, count itself as one
                if (classInfo.isAbstract()) {
                    packageInfo.incrementAbstractClass();
                }
                if (classInfo.isUsingOtherPackages()) {
                    packageInfo.incrementEfferentCouplings();
                }

            } else if (listOfFiles[i].isDirectory()) {
                if (listOfFiles[i].getName().equalsIgnoreCase("test"))
                    continue;

                calculateMetricsRecursive(directoryName+"/"+listOfFiles[i].getName());
            }
        }
    }

    private boolean notValidJavaFile(String filename) {
        return filename.equalsIgnoreCase(".DS_Store") ||
                filename.equalsIgnoreCase("package-info.java") ||
                filename.equalsIgnoreCase("module-info.java") ||
                !filename.endsWith(".java");
    }


    private int getFirstCapitalIndex(String name) {
        char[] letters = name.toCharArray();
        for (int i = 0; i < letters.length; i++) {
            if (letters[i] >= 'A' && letters[i] <= 'Z') {
                if (letters[i-1] == '.')
                    return i;
            }
        }
        return -1;
    }

}
