package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {
    public static String reportPath;
    private static ExtentReports extentReports;

    private ExtentReportManager() {
    }

    public static ExtentReports getReportInstance() { // Singleton
        if (extentReports == null) {
            String timestamp = new SimpleDateFormat(UtilsConstants.DATETIME_FORMAT).format(new Date());
            reportPath = "output/reports/ExtentReport_" + timestamp + ".html";

            ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);

            reporter.config().setDocumentTitle("Automation IHM Test Report");
            reporter.config().setReportName("Test Execution Report");

            extentReports = new ExtentReports();
            extentReports.attachReporter(reporter);
        }
        return extentReports;
    }

    public static ExtentTest createTest(String testName) {
        ExtentTest extentTest = getReportInstance().createTest(testName);
        Log.extendedInfo(extentTest, "Starting \"" + testName + "\"...");

        return extentTest;
    }

    public static String takeScreenshot(CustomFireFoxDriver driver, String screenshotName) {
        try {
            File src = driver.getScreenshotAs(OutputType.FILE);
            String path = "output/screenshots/" + screenshotName + "_" + new SimpleDateFormat(UtilsConstants.DATETIME_FORMAT).format(new Date()) + ".png";
            FileUtils.copyFile(src, new File(path));
            Log.info("Screenshot captures, PATH: " + path);
            return path;
        } catch (Exception e) {
            Log.error("Can't save report screenshot, error: " + e.getMessage());
            return null;
        }
    }
}
