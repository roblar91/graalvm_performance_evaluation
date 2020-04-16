package benchmark;

import benchmark.results.ResultsManager;
import benchmark.results.ResultsParser;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;

public class Main {
    private static final String TEST_RESULTS_PATH = "/home/knickus/graalvm_performance/results/2020-04-15/raw";
    private static final String TOML_RESULTS_PATH = "/home/knickus/graalvm_performance/results/2020-04-15/results.toml";
    private static ResultsManager manager = new ResultsManager();
    private static ResultsParser parser = new ResultsParser();

    public static void main(String[] args) throws Exception {
        parser.parseFolder(manager, new File(TEST_RESULTS_PATH), 20);
        manager.saveAsToml(new File(TOML_RESULTS_PATH));
        manager.prettyPrintAll();

//        var avroraOpenJDK8 = manager.getResults(Benchmark.AVRORA, JVM.OPENJDK_8, MeasurementType.STEADY_STATE);
//        var avroraGraalCE8 = manager.getResults(Benchmark.AVRORA, JVM.GRAALVM_CE_8, MeasurementType.STEADY_STATE);
//        var avroraGraalEE8 = manager.getResults(Benchmark.AVRORA, JVM.GRAALVM_EE_8, MeasurementType.STEADY_STATE);
//
//        var mean = avroraOpenJDK8.getMean();
//
//        avroraOpenJDK8 = avroraOpenJDK8.createNormalizedResults(mean);
//        avroraGraalCE8 = avroraGraalCE8.createNormalizedResults(mean);
//        avroraGraalEE8 = avroraGraalEE8.createNormalizedResults(mean);
//
//        avroraOpenJDK8.prettyPrint();
//        avroraGraalCE8.prettyPrint();
//        avroraGraalEE8.prettyPrint();
//
//        System.out.println("avroraOpenJDK8.isConfidenceIntervalOverlapping(ConfidenceLevel.PERCENT_95, avroraGraalCE8) = " + avroraOpenJDK8.isConfidenceIntervalOverlapping(ConfidenceLevel.PERCENT_95, avroraGraalCE8));
//        System.out.println("avroraOpenJDK8.isConfidenceIntervalOverlapping(ConfidenceLevel.PERCENT_95, avroraGraalEE8) = " + avroraOpenJDK8.isConfidenceIntervalOverlapping(ConfidenceLevel.PERCENT_95, avroraGraalEE8));
    }
}
