package benchmark.results;

import benchmark.Benchmark;
import benchmark.JVM;

import java.util.ArrayList;
import java.util.List;

/**
 * A class meant for storing performance measurements procured by running a specific {@link Benchmark} using a specific {@link JVM}.
 */
public class Results {
    private List<Integer> startupTimes = new ArrayList<>();
    private List<Integer> steadyTimes = new ArrayList<>();
    private Benchmark benchmark;
    private JVM jvm;

    /**
     * Creates a new {@link Results} object to be used for storing data procured using the specified benchmark and JVM.
     * @param benchmark The benchmark ran when procuring the data
     * @param jvm The JVM used when procuring the data
     */
    public Results(Benchmark benchmark, JVM jvm) {
        this.benchmark = benchmark;
        this.jvm = jvm;
    }

    /**
     * Gets the {@link Benchmark} relevant for the data stored in this object.
     * @return The benchmark
     */
    public Benchmark getBenchmark() {
        return benchmark;
    }

    /**
     * Gets the {@link JVM} relevant for the data stored in this object.
     * @return The JVM
     */
    public JVM getJvm() {
        return jvm;
    }

    /**
     * Saves a startup time to this {@link Results}.
     * @param millis The measured startup time in milliseconds
     */
    public void addStartupResult(int millis) {
        startupTimes.add(millis);
    }

    /**
     * Saves a steady state time to this {@link Results}.
     * @param millis The measured steady state time in milliseconds
     */
    public void addSteadyResult(int millis) {
        steadyTimes.add(millis);
    }

    /**
     * Gets the average of all values saved via {@link #addStartupResult(int)}.
     * @return The average in or -1 if no values have been saved
     */
    public int getStartupAverage() {
        if(startupTimes.size() == 0) {
            return -1;
        }

        return getAverage(startupTimes);
    }

    /**
     * Gets the average of all values saved via {@link #addSteadyResult(int)}.
     * @return The average or -1 if no values have been saved
     */
    public int getSteadyAverage() {
        if(steadyTimes.size() == 0) {
            return -1;
        }

        return getAverage(steadyTimes);
    }

    /**
     * Prints all relevant information stored in this object in an easy to read format.
     */
    public void prettyPrint() {
        System.out.println("******************************************");
        System.out.format("%20s : %s", "Benchmark", benchmark + "\n");
        System.out.format("%20s : %s", "JVM: ", jvm + "\n");
        System.out.format("%20s : %s", "Startup average: ", getStartupAverage() + "ms\n");
        System.out.format("%20s : %s", "Steady average: ", getSteadyAverage() + "ms\n");
    }

    private int getAverage(List<Integer> list) {
        var total = 0;
        for(int i : list) {
            total += i;
        }

        return total / list.size();
    }
}
