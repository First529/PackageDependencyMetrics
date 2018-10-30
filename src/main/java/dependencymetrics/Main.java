package dependencymetrics;

import java.util.Collection;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        PackageDependencyMetric metric = new PackageDependencyMetric();
        metric.calculateMetrics("/Users/usa/Desktop/log4j");

        Map<String,PackageInfo> map = metric.getPackageInfoMap();
        Collection<PackageInfo> packageInfoSet = map.values();
        for (PackageInfo pkg : packageInfoSet) {
            System.out.println(pkg);
        }

        DataSource data = new DataSource("log4j.csv");
        data.write(map);

    }
}
