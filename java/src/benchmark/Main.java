package benchmark;

import benchmark.results.ResultsManager;
import benchmark.results.ResultsParser;

import java.io.File;

public class Main {
    private static final String TEST_RESULTS_PATH = "/home/knickus/graalvm_performance/results";
    private static ResultsManager manager = new ResultsManager();
    private static ResultsParser parser = new ResultsParser();

    public static void main(String[] args) throws Exception {
        parser.parseFolder(manager, new File(TEST_RESULTS_PATH));
        manager.prettyPrintAll();
    }
}
