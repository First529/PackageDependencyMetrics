package dependencymetrics;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class PackageInfo {

    private static final NumberFormat formatter = new DecimalFormat("#0.00");
    private String packageName;

    /**
     * Ca (afferent couplings):
     * The number of classes outside this component that depend on
     * classes within this component
     */
    private int ca;

    /**
     * Ce (efferent couplings):
     * The number of classes inside this component that depend on
     * classes outside this component
     */
    private int ce;

    /**
     * Na is the number of abstract classes in the component.
     * Remember, an abstract class is a class with at least
     * one abstract method and cannot be instantiated:
     */
    private int na;

    /**
     * Nc is the number of classes in the component.
     */
    private int nc;

    private List<ClassInfo> allClassInfo;

    public PackageInfo(String packageName) {
        this.packageName = packageName;
        this.allClassInfo = new ArrayList<>();
    }

    public String getPackageName() {
        return packageName;
    }

    public void incrementEfferentCouplings() {
        ce++;
    }

    public void incrementAfferentCouplings() {
        ca++;
    }

    public double getInstability() {
        return ((double)ce) / ((double)(ca + ce));
    }

    public double getAbstractness() {
        return ((double) na) / ((double) nc);
    }

    public double getDistance() {
        return Math.abs(getAbstractness() + getInstability() - 1);
    }

    public int getClassCount() {
        return nc;
    }

    public void incrementClass() {
        nc++;
    }

    public void incrementAbstractClass() {
        na++;
    }

    public void addClassToPackage(ClassInfo classInfo) {
        allClassInfo.add(classInfo);
    }

    public List<ClassInfo> getAllClassInfo() {
        return allClassInfo;
    }

    @Override
    public String toString() {
        return packageName + " [na = " + na + ", nc = " + nc +
                ", A = " + formatter.format(getAbstractness()) + "]" +
                " [ca = " + ca + ", ce = " + ce +
                ", I = " + formatter.format(getInstability()) +
                "] D = " + formatter.format(getDistance());
    }
}
