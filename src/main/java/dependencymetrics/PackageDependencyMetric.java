package dependencymetrics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;

public class PackageDependencyMetric {

    private Map<String, PackageInfo> packageInfoMap;
    private List<ClassInfo> allClassInfos;

    public PackageDependencyMetric() {
        packageInfoMap = new TreeMap<>();
        allClassInfos = new ArrayList<>();
    }

    public void calculateMetrics(String directoryName) {
        calculateMetricsRecursive(directoryName);

        // read import
        // see which package it imports
        // count itself as using package
        for (ClassInfo classInfo : allClassInfos) {
            Set<String> importList = classInfo.getPackagesImported();
            for (String importPackage : importList) {
                PackageInfo p = packageInfoMap.get(importPackage);
                if (p != null)
                    p.incrementAfferentCouplings();
            }
        }
    }


    private void calculateMetricsRecursive(String directoryName) {
        File folder = new File(directoryName);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {

                if (listOfFiles[i].getName().equalsIgnoreCase(".DS_Store") ||
                        listOfFiles[i].getName().equalsIgnoreCase("package-info.java") ||
                        !listOfFiles[i].getName().endsWith(".java"))
                    continue;

                ClassInfo classInfo = new ClassInfo(listOfFiles[i]);
                allClassInfos.add(classInfo);

                // read package name
                // add package name if doesn't exist
                // count itself as within package
                String packageName = classInfo.getPackageName();
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
                calculateMetricsRecursive(directoryName+"/"+listOfFiles[i].getName());
            }
        }

    }

    public List<ClassInfo> getAllClassInfo() {
        return allClassInfos;
    }

    public Map<String,PackageInfo> getPackageInfoMap() {
        return packageInfoMap;
    }
}
