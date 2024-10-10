package Nashtech;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestDataCollector {
    private List<TestResult> testResults;

    public TestDataCollector() {
        this.testResults = new ArrayList<>();
    }

    public void collectTestResult(String testName, boolean passed) {
        testResults.add(new TestResult(testName, passed));
    }

    public List<TestResult> getTestResults() {
        return testResults;
    }

    public void writeResultsToArff(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("@relation testResults\n");
            writer.write("@attribute testName string\n");
            writer.write("@attribute passed {true, false}\n");
            writer.write("@data\n");
            for (TestResult result : testResults) {
                writer.write("'" + result.getTestName() + "'," + (result.isPassed() ? "true" : "false") + "\n");
            }
        }
    }
}
