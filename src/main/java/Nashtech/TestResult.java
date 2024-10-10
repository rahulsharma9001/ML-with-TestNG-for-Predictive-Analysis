package Nashtech;

public class TestResult {
    private String testName;
    private boolean passed;

    public TestResult(String testName, boolean passed) {
        this.testName = testName;
        this.passed = passed;
    }

    public String getTestName() {
        return testName;
    }

    public boolean isPassed() {
        return passed;
    }
}
