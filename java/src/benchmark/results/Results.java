package benchmark.results;

import benchmark.Benchmark;
import benchmark.ConfidenceLevel;
import benchmark.JVM;
import benchmark.MeasurementType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A class meant for storing performance measurements procured by running a specific {@link Benchmark} using a specific {@link JVM}.
 */
public class Results {
    private final List<Double> data = new ArrayList<>();
    private final Benchmark benchmark;
    private final JVM jvm;
    private final MeasurementType type;

    /**
     * Creates a new {@link Results} object to be used for storing data of the specified type,
     * procured using the specified benchmark and JVM.
     *
     * @param benchmark The benchmark ran when procuring the data
     * @param jvm       The JVM used when procuring the data
     * @param type      The type of the data
     */
    public Results(Benchmark benchmark, JVM jvm, MeasurementType type) {
        this.benchmark = benchmark;
        this.jvm = jvm;
        this.type = type;
    }

    /**
     * Gets the {@link Benchmark} relevant for the data stored in this object.
     *
     * @return The benchmark
     */
    public Benchmark getBenchmark() {
        return benchmark;
    }

    /**
     * Gets the {@link JVM} relevant for the data stored in this object.
     *
     * @return The JVM
     */
    public JVM getJvm() {
        return jvm;
    }

    /**
     * Gets the {@link MeasurementType} of the data stored in this object.
     *
     * @return The type
     */
    public MeasurementType getType() {
        return type;
    }

    /**
     * Saves a podouble of data to this {@link Results}.
     *
     * @param millis The measured time in milliseconds
     */
    public void addData(double millis) {
        data.add(millis);
    }

    /**
     * Gets the mean of all values saved via {@link #addData(double)}.
     *
     * @return The mean or -1 if no values have been saved
     */
    public double getMean() {
        if(getSize() == 0) {
            return -1;
        }

        var total = 0;
        for(double i : data) {
            total += i;
        }

        return total / getSize();
    }

    /**
     * Gets the median of all values saved via {@link #addData(double)}.
     *
     * @return The median or -1 if no values have been saved
     */
    public double getMedian() {
        if(getSize() == 0) {
            return -1;
        }

        data.sort(Double::compare);

        double median;

        if(getSize() % 2 == 0) {
            double i1 = data.get(getSize() / 2 - 1);
            double i2 = data.get(getSize() / 2);
            median = (i1 + i2) / 2;
        } else {
            median = data.get(getSize() / 2);
        }

        return median;
    }

    /**
     * Gets the max of all values saved via {@link #addData(double)}.
     *
     * @return The max or -1 if no values have been saved
     */
    public double getMax() {
        if(getSize() == 0) {
            return -1;
        }

        data.sort(Double::compare);
        return data.get(getSize() - 1);
    }

    /**
     * Gets the min of all values saved via {@link #addData(double)}.
     *
     * @return The min or -1 if no values have been saved
     */
    public double getMin() {
        if(getSize() == 0) {
            return -1;
        }

        data.sort(Double::compare);
        return data.get(0);
    }

    /**
     * Gets the number of values saved via {@link #addData(double)}.
     *
     * @return The number of values saved
     */
    public int getSize() {
        return data.size();
    }

    /**
     * Gets the standard deviation of all values saved via {@link #addData(double)}.
     *
     * @return The standard deviation or -1 if no values have been saved
     */
    public double getStandardDeviation() {
        if(getSize() == 0) {
            return -1;
        }

        double mean = getMean();
        double sumDistanceFromMeanSquared = 0;
        for(double i : data) {
            sumDistanceFromMeanSquared += Math.pow(i - mean, 2);
        }

        return Math.sqrt(sumDistanceFromMeanSquared / getSize());
    }

    /**
     * Gets the standard error of all values saved via {@link #addData(double)}.
     *
     * @return The standard error or -1 if no values have been saved
     */
    public double getStandardError() {
        if(getSize() == 0) {
            return -1;
        }

        return getStandardDeviation() / Math.sqrt(getSize());
    }

    /**
     * Gets the margin of error given the specified confidence level.
     * @param confidenceLevel The confidence level to calculate for
     * @return The margin of error
     */
    public double getMarginOfError(ConfidenceLevel confidenceLevel) {
        return confidenceLevel.getZ() * getStandardDeviation() / Math.sqrt(getSize());
    }

    /**
     * Gets the upper bound of the confidence interval for the specified confidence level.
     * @param confidenceLevel The confidence level to calculate for
     * @return The upper bound
     */
    public double getConfidenceIntervalUpper(ConfidenceLevel confidenceLevel) {
        return getMean() + getMarginOfError(confidenceLevel);
    }

    /**
     * Gets the lower bound of the confidence interval for the specified confidence level.
     * @param confidenceLevel The confidence level to calculate for
     * @return The lower bound
     */
    public double getConfidenceIntervalLower(ConfidenceLevel confidenceLevel) {
        return getMean() - getMarginOfError(confidenceLevel);
    }

    /**
     * Prdoubles all relevant information stored in this object in an easy to read format.
     */
    public void prettyPrint() {
        var zeroDecimals = new DecimalFormat("#");
        var oneDecimal = new DecimalFormat("#.0");

        System.out.println("*****************************************************************");
        System.out.format("%30s : %s", "Benchmark", benchmark.getName() + "\n");
        System.out.format("%30s : %s", "JVM", jvm.getName() + "\n");
        System.out.format("%30s : %s", "Type", type.getName() + "\n");
        System.out.println("     --------------------------------------------------");
        System.out.format("%30s : %s", "Sample size",
                          zeroDecimals.format(getSize()) + "\n");
        System.out.format("%30s : %s", "Mean",
                          zeroDecimals.format(getMean()) + " ms\n");
        System.out.format("%30s : %s", "Median",
                          zeroDecimals.format(getMedian()) + " ms\n");
        System.out.format("%30s : %s", "Max",
                          zeroDecimals.format(getMax()) + " ms\n");
        System.out.format("%30s : %s", "Min",
                          zeroDecimals.format(getMin()) + " ms\n");
        System.out.format("%30s : %s", "Standard deviation",
                          oneDecimal.format(getStandardDeviation()) + " ms\n");
        System.out.format("%30s : %s", "Standard error",
                          oneDecimal.format(getStandardError()) + " ms\n");

        for(ConfidenceLevel cl : ConfidenceLevel.values()) {
            System.out.println();
            System.out.println("     -------- Confidence level : " + cl.getName() + " ------------------");
            System.out.format("%30s : %s", "Margin of error",
                              oneDecimal.format(getMarginOfError(cl)) + " ms\n");
            System.out.format("%30s : %s", "Confidence interval lower",
                              oneDecimal.format(getConfidenceIntervalLower(cl)) + " ms\n");
            System.out.format("%30s : %s", "Confidence interval upper",
                              oneDecimal.format(getConfidenceIntervalUpper(cl)) + " ms\n");
        }

        System.out.println("*****************************************************************");
    }
}
