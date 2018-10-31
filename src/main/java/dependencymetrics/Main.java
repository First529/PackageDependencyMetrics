package dependencymetrics;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        PackageDependencyMetric metric = new PackageDependencyMetric();
        metric.calculateMetrics("/Users/usa/Desktop/hadoop",
                "org.apache");

        Map<String,PackageInfo> map = metric.getPackageInfoMap();
        List<PackageInfo> packageInfoList = new ArrayList<>();
        packageInfoList.addAll(map.values());

        Collections.sort(packageInfoList, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo o1, PackageInfo o2) {
                return o2.getClassCount() - o1.getClassCount();
            }
        });

        DataSource data = new DataSource("hadoop.csv");
        data.write(packageInfoList);
    }
}
