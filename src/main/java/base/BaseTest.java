package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utils.CustomFireFoxDriver;
import utils.EmailUtils;
import utils.ExtentReportManager;
import utils.Log;

import java.lang.reflect.Method;
import java.time.Duration;

public class BaseTest {
    protected Dotenv dotenv = Dotenv.load();
    protected CustomFireFoxDriver driver;
    protected Actions actions;
    protected ExtentTest extentTest;

    protected static ExtentReports extentReports;

    @BeforeSuite
    public void setupReport() {
        extentReports = ExtentReportManager.getReportInstance();
    }

    @AfterSuite
    public void tearDownReport() {
        Log.info("---------------- All test ended ----------------");
        extentReports.flush();
        String reportPath = ExtentReportManager.reportPath;
        Log.info("Report generated at: " + reportPath);
        EmailUtils.sendMailReport(reportPath);
    }

    // TODO: add option to reuse same browser
    @BeforeMethod
    public void setup(Method method) {
        // Help to find quickly in log files
        Log.info("---------------- \"" + method.getName() + "\" started ---------------- ");

        Log.info("Setting up WebDriver");
        WebDriverManager.firefoxdriver().setup();

        // Init vars
        driver = new CustomFireFoxDriver(getFirefoxOptions());
        driver.get(dotenv.get("LOGIN_URL"));
        actions = new Actions(driver);

        // changing the property of the navigator value for webdriver to undefined
        driver.executeScript("Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
    }

    private static FirefoxOptions getFirefoxOptions() {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("dom.disable_beforeunload", true);
        profile.setPreference("browser.privatebrowsing.autostart", true);
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.popups.showPopupBlocker", false);
        profile.setPreference("dom.webnotifications.enabled", false);
        profile.setPreference("plugin.scan.plid.all", false);
        profile.setPreference("extensions.blocklist.enabled", false);

        // Options
        FirefoxOptions options = new FirefoxOptions();
        options.setProfile(profile);
        options.addArguments("--disable-blink-features=AutomationControlled", "--start-maximized");
        return options;
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        extentTest.assignCategory(result.getMethod().getGroups());

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                Log.extendedSuccess(extentTest, "Test \"" + result.getName() + "\" has passed.");
                break;
            case ITestResult.FAILURE:
                String screenshotPath = ExtentReportManager.takeScreenshot(driver, result.getName());
                Log.extendedError(extentTest,
                        "Test \"" + result.getName() + "\" failed, screenshot attached.",
                        screenshotPath);
                break;
            case ITestResult.SKIP:
                extentTest.skip("Test \"" + result.getName() + "\" skipped.");
                break;
            default:
                String message = "Something wrong appended, status unknow.";
                extentTest.skip(message);
                Log.warn(message);
        }

        if (driver != null) {
            Log.info("Closing browser...");
            driver.quit();
        }
    }
}