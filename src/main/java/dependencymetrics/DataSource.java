package dependencymetrics;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Map;

public class DataSource {
    private static final NumberFormat formatter = new DecimalFormat("#0.00");

    private String filename;

    public DataSource(String filename) {
        this.filename = filename;
    }

    public void write(Map<String, PackageInfo> packageInfoMap) {
        FileWriter file;
        try {
            file = new FileWriter(filename);
            PrintWriter out = new PrintWriter(file);

            Collection<PackageInfo> packageInfoSet = packageInfoMap.values();
            out.println("package,NC,A,I,D");
            for (PackageInfo pkg : packageInfoSet) {
                out.print(pkg.getPackageName().replace("org.apache.logging.","") + ",");
                out.print(pkg.getClassCount() + ",");
                out.print(formatter.format(pkg.getAbstractness()) + ",");
                out.print(formatter.format(pkg.getInstability()) + ",");
                out.println(formatter.format(pkg.getDistance()));
            }
            out.flush();
            out.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
