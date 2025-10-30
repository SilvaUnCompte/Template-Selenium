package utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static final Logger logger = LogManager.getLogger(Log.class);

    private Log() {
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void extendedInfo(ExtentTest extentTest, String message) {
        extentTest.info(message);
        info(message);
    }

    public static void extendedWarn(ExtentTest extentTest, String message) {
        extentTest.warning(message);
        warn(message);
    }

    public static void extendedError(ExtentTest extentTest, String message, String screenshotPath) {
        error(message);

        if (screenshotPath != null) {
            extentTest.fail(message, MediaEntityBuilder.createScreenCaptureFromPath("../../" + screenshotPath).build());
        } else {
            extentTest.fail(message);
        }
    }

    public static void extendedSuccess(ExtentTest extentTest, String message) {
        extentTest.pass(message);
        info(message);
    }
}
