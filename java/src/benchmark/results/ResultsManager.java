package benchmark.results;

import benchmark.Benchmark;
import benchmark.JVM;
import benchmark.MeasurementType;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * A class meant for storing and retrieving multiple {@link Results}.
 */
public class ResultsManager {
    private static class ResultsKey implements Comparable<ResultsKey>{
        Benchmark benchmark;
        JVM jvm;
        MeasurementType type;

        ResultsKey(Benchmark benchmark, JVM jvm, MeasurementType type) {
            this.benchmark = benchmark;
            this.jvm = jvm;
            this.type = type;
        }

        @Override
        public int hashCode() {
            return benchmark.hashCode() + jvm.hashCode() + type.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof ResultsKey) {
                ResultsKey other = (ResultsKey) obj;

                return benchmark.equals(other.benchmark) && jvm.equals(other.jvm) && type.equals(other.type);
            }

            return false;
        }

        @Override
        public int compareTo(ResultsKey other) {
            if(benchmark.compareTo(other.benchmark) == 0) {
                if(jvm.compareTo(other.jvm) == 0) {
                    return type.compareTo(other.type);
                }
                return jvm.compareTo(other.jvm);
            }
            return benchmark.compareTo(other.benchmark);
        }
    }

    private Map<ResultsKey, Results> resultsMap = new TreeMap<>();

    /**
     * Returns a {@link Results} object that can be used for storing or retrieving benchmark measurments.
     * If no {@link Results} object yet exists for the given {@link Benchmark}, {@link JVM} and
     * {@link MeasurementType} key tuple, one will be created.
     * @param benchmark The benchmark to be used as a key
     * @param jvm The JVM to be used as a key
     * @param type The type of the data
     * @return The results object
     */
    public Results getResults(Benchmark benchmark, JVM jvm, MeasurementType type) {
        var key = new ResultsKey(benchmark, jvm, type);
        var results = resultsMap.get(key);

        if(results == null) {
            results = new Results(benchmark, jvm, type);
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

    /**
     * Returns an iterator
     * @return An iterator over all items
     */
    public Iterator<Results> getAllResults() {
        return resultsMap.values().iterator();
    }
}
