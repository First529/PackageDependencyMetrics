package dependencymetrics;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class DataSource {
    private static final NumberFormat formatter = new DecimalFormat("#0.00");

    private String filename;

    public DataSource(String filename) {
        this.filename = filename;
    }

    public void write(List<PackageInfo> packageInfoList) {
        FileWriter file;
        try {
            file = new FileWriter(filename);
            PrintWriter out = new PrintWriter(file);

            out.println("id,package,NC,A,I,D");
            int id = 0;
            for (PackageInfo pkg : packageInfoList) {
                out.print(++id + ",");
                out.print(pkg.getPackageName() + ",");
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
