package benchmark;

public enum ConfidenceLevel {
    PERCENT_99("99%", 2.576),
    PERCENT_98("98%", 2.326),
    PERCENT_95("95%", 1.960),
    PERCENT_90("90%", 1.645),
    PERCENT_85("85%", 1.440),
    PERCENT_80("80%", 1.282);

    private String name;
    private double z;

    ConfidenceLevel(String name, double z) {
        this.name = name;
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public double getZ() {
        return z;
    }
}
