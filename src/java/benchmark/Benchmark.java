package benchmark;

public enum Benchmark {
    AVRORA("avrora"),
    FOP("fop"),
    H2("h2"),
    JYTHON("jython"),
    LUINDEX("luindex"),
    LUSEARCH_FIX("lusearch-fix"),
    PMD("pmd"),
    SUNFLOW("sunflow"),
    TRADEBEANS("tradebeans"),
    XALAN("xalan");

    private String name;

    Benchmark(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Benchmark getBenchmark(String name) {
        for(Benchmark b : Benchmark.values()) {
            if(b.name.equals(name)) {
                return b;
            }
        }

        return null;
    }
}