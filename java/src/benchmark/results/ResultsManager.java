package benchmark.results;

import benchmark.Benchmark;
import benchmark.JVM;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * A class meant for storing and retrieving multiple {@link Results}.
 */
public class ResultsManager {
    private static class TestKey implements Comparable<TestKey>{
        Benchmark benchmark;
        JVM jvm;

        TestKey(Benchmark benchmark, JVM jvm) {
            this.benchmark = benchmark;
            this.jvm = jvm;
        }

        @Override
        public int hashCode() {
            return benchmark.hashCode() + jvm.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof TestKey) {
                TestKey other = (TestKey) obj;

                return benchmark.equals(other.benchmark) && jvm.equals(other.jvm);
            }

            return false;
        }

        @Override
        public int compareTo(TestKey other) {
            if(benchmark.compareTo(other.benchmark) == 0) {
                return jvm.compareTo(other.jvm);
            }

            return benchmark.compareTo(other.benchmark);
        }
    }

    private Map<TestKey, Results> resultsMap = new TreeMap<>();

    /**
     * Returns a {@link Results} object that can be used for storing or retrieving benchmark measurments.
     * If no {@link Results} object yet exists for the given {@link Benchmark} and {@link JVM} key pair,
     * one will be created.
     * @param benchmark The benchmark to be used as a key
     * @param jvm The JVM to be used as a key
     * @return The results object
     */
    public Results getResults(Benchmark benchmark, JVM jvm) {
        var key = new TestKey(benchmark, jvm);
        var results = resultsMap.get(key);

        if(results == null) {
            results = new Results(benchmark, jvm);
            resultsMap.put(key, results);
        }

        return results;
    }

    /**
     * Calls {@link Results#prettyPrint()} on all {@link Results} stored in this object.
     */
    public void prettyPrintAll() {
        resultsMap.values().forEach(Results::prettyPrint);
    }

    public Iterator<Results> getAllResults() {
        return resultsMap.values().iterator();
    }
}
