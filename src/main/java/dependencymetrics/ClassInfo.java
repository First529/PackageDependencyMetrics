package dependencymetrics;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class ClassInfo {

    private File file;
    private String packageName;
    private String className;
    private boolean isAbstract;
    private boolean isUsingOtherPackages;
    private Set<String> packagesImported;

    public ClassInfo(File file) {
        this.file = file;
        this.isAbstract = false;
        this.isUsingOtherPackages = false;
        this.packagesImported = new HashSet<>();
        this.className = file.getName();
        collectInfo();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public boolean isUsingOtherPackages() {
        return isUsingOtherPackages;
    }

    public Set<String> getPackagesImported() {
        return packagesImported;
    }

    private void collectInfo() {
        FileReader reader;

        try {
            reader = new FileReader(file);
            BufferedReader in = new BufferedReader(reader);

            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("package")) {
                    String[] data = line.split(" ");
                    packageName = data[1].substring(0, data[1].length()-1);
                }
                else if (line.startsWith("import org.apache.logging.log4j")) {
                    isUsingOtherPackages = true;
                    String[] data = line.split(" ");
                    String useClassName = data[1].substring(0, data[1].length()-1);
                    String usePackageName = useClassName.substring(0, useClassName.lastIndexOf("."));
                    packagesImported.add(usePackageName);

                }
                else if (line.startsWith("public abstract class")) {
                    isAbstract = true;
                    break;
                }
                else if (line.startsWith("public interface")) {
                    isAbstract = true;
                    break;
                }


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
