package benchmark.results;

import benchmark.Benchmark;
import benchmark.JVM;
import benchmark.MeasurementType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is only meant to parse files populated with output from the DaCapo suite.
 * The files must also be tagged with '#JVM jvm_name' and '#BENCHMARK benchmark_name'
 * where 'jvm_name' and 'benchmark_name' generates a result when inserted into
 * {@link JVM#getJVM(String)} or {@link Benchmark#getBenchmark(String)} respectively.
 */
public class ResultsParser {
    /**
     * Parses all files located in the provided folder and stores the data in the provided {@link ResultsManager}.
     *
     * @param manager The {@link ResultsManager} where results should be stored
     * @param folder  The root folder
     * @param warmupIterations The number of iterations to be discarded when recording steady state data
     * @throws IOException If an error occurs while reading the file
     */
    public void parseFolder(ResultsManager manager, File folder, int warmupIterations) throws IOException {
        if(folder == null || !folder.isDirectory()) {
            throw new IOException("Provided folder is not a directory");
        }

        var files = folder.listFiles();
        if(files != null) {
            for(File file : files) {
                parseFile(manager, file, warmupIterations);
            }
        }
    }

    /**
     * Parses a file and stores the data in the provided {@link ResultsManager}.
     *
     * @param manager The {@link ResultsManager} where results should be stored
     * @param file    The file
     * @param warmupIterations The number of iterations to be discarded when recording steady state data
     * @throws IOException If an error occurs while reading the file
     */
    public void parseFile(ResultsManager manager, File file, int warmupIterations) throws IOException {
        if(file == null || !file.isFile()) {
            throw new IOException("Provided file is not valid");
        }

        var reader = new BufferedReader(new FileReader(file));
        var lines = new ArrayList<String>();

        while(reader.ready()) {
            lines.add(reader.readLine());
        }

        var jvm = JVM.getJVM(getValue(lines, "#JVM"));
        if(jvm == null) {
            throw new IOException("JVM specified in " + file.getAbsolutePath() + " not defined");
        }

        var benchmark = Benchmark.getBenchmark(getValue(lines, "#BENCHMARK"));
        if(benchmark == null) {
            throw new IOException("Benchmark specified in " + file.getAbsolutePath() + " not defined");
        }

        populateResults(lines,
                        manager.getResults(benchmark, jvm, MeasurementType.STARTUP),
                        manager.getResults(benchmark, jvm, MeasurementType.STEADY_STATE),
                        warmupIterations);

        reader.close();
    }

    private String getValue(List<String> lines, String key) throws IOException {
        for(String line : lines) {
            if(line.startsWith(key)) {
                return line.substring(key.length()).stripLeading();
            }
        }

        throw new IOException("No value for key '" + key + "' found");
    }

    private void populateResults(List<String> lines,
                                 Results startupResults,
                                 Results steadyResults,
                                 int warmupIterations) {
        for(String line : lines) {
            if(line.contains("PASSED")) {
                var millis = Integer.parseInt(line.split("PASSED")[1].replaceAll("[\\D]", ""));
                steadyResults.addData(millis);
            } else if(line.contains("completed warmup")) {
                var strings = line.split("completed warmup");
                strings = strings[1].split(" in ");

                var run = Integer.parseInt(strings[0].replaceAll("[\\D]", ""));
                var millis = Integer.parseInt(strings[1].replaceAll("[\\D]", ""));
                if(run == 1) {
                    startupResults.addData(millis);
                }
                if(run > warmupIterations) {
                    steadyResults.addData(millis);
                }
            }
        }
    }
}
