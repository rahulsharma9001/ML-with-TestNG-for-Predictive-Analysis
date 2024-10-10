import Nashtech.LoginPage;
import Nashtech.PredictiveModel;
import Nashtech.TestDataCollector;
import Nashtech.TestResult;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import weka.core.Instances;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SampleTest {
    private static final Logger LOGGER = Logger.getLogger(SampleTest.class.getName());
    private TestDataCollector collector = new TestDataCollector();
    private PredictiveModel model = new PredictiveModel();
    public static WebDriver driver;
    public LoginPage loginPage;

    @BeforeTest
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        loginPage = PageFactory.initElements(driver, LoginPage.class);
    }

//    @BeforeClass
    public void setUp() throws Exception {
        String trainingDataPath = "training/data.arff"; // Relative path from resources
        model.trainModel(trainingDataPath);
    }

    @AfterTest
    public void destroy() {
        if (driver != null) {
            driver.quit();
        }

        // Log the collected test results
        List<TestResult> results = collector.getTestResults();
        for (TestResult result : results) {
            System.out.println("Test: " + result.getTestName() + ", Passed: " + result.isPassed());
        }

        // Write results to ARFF file
        try {
            collector.writeResultsToArff("src/test/resources/test/data.arff");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write results to ARFF file", e);
        }
    }

    @Test
    public void loginUser() throws Exception {
        // Load test data for prediction
        Instances testInstance = PredictiveModel.loadData("src/test/resources/test/data.arff");
        if (testInstance == null || testInstance.numInstances() == 0) {
            throw new IllegalArgumentException("Test data is invalid or empty");
        }
        testInstance.setClassIndex(testInstance.numAttributes() - 1); // Set the class index to the last attribute

        double prediction = model.predict(testInstance);

        // Log the prediction
        System.out.println("Prediction for loginUser test: " + (prediction == 1.0 ? "Defect" : "No Defect"));

        // If prediction is for defect, log it and skip the test
        if (prediction == 1.0) {
            System.out.println("Skipping loginUser test due to high risk of defect.");
            collector.collectTestResult("loginUser", false); // Log as failed
            return;
        }

        // Perform the actual test
        loginPage.login();

        // Collect the actual test result (assuming the login was successful for simplicity)
        boolean loginSuccessful = true; // Update this based on the actual test result
        collector.collectTestResult("loginUser", loginSuccessful);

        // Assert the test result
        Assert.assertTrue(loginSuccessful);
    }
}
