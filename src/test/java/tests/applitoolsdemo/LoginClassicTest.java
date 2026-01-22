package tests.applitoolsdemo;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.EyesRunner;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.applitoolsdemo.DashboardPage;
import pages.applitoolsdemo.LoginPage;

import java.time.Duration;

public class LoginClassicTest {
    private WebDriver driver;
    private Eyes eyes;
    private static Configuration suiteConfig;
    private static EyesRunner testRunner;

    @BeforeClass
    public void setUpClass() {
        // Set ChromeDriver system property if needed
        // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        BatchInfo myBatch = new BatchInfo("Java Selenium Classic Runner");
        suiteConfig = new Configuration();
        suiteConfig.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
        suiteConfig.setBatch(myBatch);
        testRunner = new ClassicRunner();
    }

    @BeforeMethod
    public void setUp(ITestResult testInfo) {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        eyes = new Eyes(testRunner);
        eyes.setConfiguration(suiteConfig);
        String testName = testInfo.getMethod().getMethodName();
        eyes.open(driver, "Applitools Demo Website", testName, new RectangleSize(1000, 800));
    }

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][] {
                {"validUser", "validPass", true},
                {"validUser", "wrongPass", false},
                {"wrongUser", "validPass", false},
                {"", "somePass", false},
                {"validUser", "", false}
        };
    }

    @Test(dataProvider = "loginData")
    public void testLoginScenarios(String username, String password, boolean isSuccessExpected) {
        driver.get("https://sandbox.applitools.com/bank");

        LoginPage login = new LoginPage(driver);
        DashboardPage dashboard = new DashboardPage(driver);

        login.enterUserName(username);
        login.enterPassword(password);
        login.clickLogin();

        if (isSuccessExpected) {
            // Wait/check dashboard loaded successfully
            // Wait until dashboard is loaded
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("time")));
            eyes.check(Target.window().withName("Dashboard after successful login"));
            // Optionally assert dashboard elements are displayed
            // Assert.assertTrue(dashboard.isLoaded());
        } else {
            // Visual check on login page or error message display
            eyes.check(Target.window().withName("Unsuccessful login"));
            // Optionally assert login error message displayed
            // Assert.assertTrue(login.isErrorMessageDisplayed());
        }
    }

    @AfterMethod
    public void tearDown() {
        if (eyes != null) {
            eyes.closeAsync();
            eyes.abortIfNotClosed();
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterClass
    public void afterClass() {
        //TestResultsSummary results = testRunner.getAllTestResults();
        //System.out.println(results);
    }
}
