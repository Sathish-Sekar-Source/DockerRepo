import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BaseTest {
    protected WebDriver driver;
    public static ExtentReports extent;
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @BeforeSuite
    public void setUpReport() {
        ExtentSparkReporter reporter = new ExtentSparkReporter("target/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);

        // Add System Info (will show in report header)
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
    }

    @Parameters("browser")
    @BeforeMethod
    public void setUp(String browser, Method method) throws MalformedURLException {
        // Create test with browser category
        ExtentTest extentTest = extent.createTest(method.getName())
                .assignCategory(browser);
        test.set(extentTest);

        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new", "--disable-gpu");
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);

        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);

        } else {
            throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        // Log browser info
        test.get().info("Started test on browser: " + browser);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {

            try {
                // Create screenshots folder if it doesn't exist
                File screenshotDir = new File("target/screenshots");
                if (!screenshotDir.exists()) {
                    screenshotDir.mkdirs();
                }

                // Unique screenshot filename: method-browser.png
                String browser = result.getMethod().getXmlTest().getParameter("browser");
                String screenshotName = result.getName() + "-" + browser + ".png";
                String screenshotPath = "screenshots/" + screenshotName;

                // Take screenshot
                TakesScreenshot ts = (TakesScreenshot) driver;
                File src = ts.getScreenshotAs(OutputType.FILE);
                Files.copy(src.toPath(), Paths.get("target/" + screenshotPath));

                // Attach to report
                test.get().addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");

            } catch (IOException e) {
                e.printStackTrace();
            }
            test.get().fail("Test Failed: " + result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.get().skip("Test Skipped: " + result.getThrowable());
        } else {
            test.get().pass("Test Passed");
        }

        if (driver != null) {
            driver.quit();
        }
    }

    @AfterSuite
    public void tearDownReport() {
        extent.flush();
    }
}