package benchmark;

public enum MeasurementType {
    STARTUP("Startup"),
    STEADY_STATE("Steady state");

    private String name;

    MeasurementType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
