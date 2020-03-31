package benchmark;

public enum JVM {
    GRAALVM_CE_8("GraalVM CE 8"),
    GRAALVM_EE_8("GraalVM EE 8"),
    GRAALVM_CE_11("GraalVM CE 11"),
    GRAALVM_EE_11("GraalVM EE 11"),
    OPENJDK_8("OpenJDK 8"),
    OPENJDK_11("OpenJDK 11");

    private String name;

    JVM(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static JVM getJVM(String name) {
        for(JVM j : JVM.values()) {
            if(j.name.equals(name)) {
                return j;
            }
        }

        return null;
    }
}