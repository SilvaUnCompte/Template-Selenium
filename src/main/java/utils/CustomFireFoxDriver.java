package utils;

import com.aventstack.extentreports.ExtentTest;
import org.jspecify.annotations.NonNull;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CustomFireFoxDriver extends FirefoxDriver {

    public CustomFireFoxDriver(FirefoxOptions opt) {
        super(opt);
    }

    @NonNull
    @Override
    public WebElement findElement(By arg) {
        try {
            WebElement element = super.findElement(arg);
            Log.debug(arg + " found.");
            return element;
        } catch (Exception e) {
            Log.error(arg + " not found.");
            throw e;
        }
    }

    public void safeClick(By by) {
        WebDriverWait wait = new WebDriverWait(this, Duration.ofSeconds(10));

        try {
            // Wait until element is clickable
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));

            // Scroll into the element
            this.executeScript(
                    "arguments[0].scrollIntoView({block: 'center', inline: 'center'});", element);

            // Sometimes needed on Firefox
            Thread.sleep(300);

            // Standard click
            element.click();

        } catch (ElementNotInteractableException e) {
            // Fallback JS click
            WebElement element = findElement(by);
            this.executeScript("arguments[0].click();", element);
        } catch (TimeoutException e) {
            Log.error("Élément non cliquable après délai d'attente: " + by.toString());
            throw new RuntimeException("Élément non cliquable après délai d'attente: " + by, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void clickOnText(ExtentTest extentTest, String text, String type) {
        if (type == null) type = "*";
        Log.extendedInfo(extentTest, "Click on \"" + text + "\"");
        safeClick(By.xpath("//" + type + "[text()='" + text + "']"));
    }

    public void clickOnPartialText(ExtentTest extentTest, String text, String type) {
        if (type == null) type = "*";
        Log.extendedInfo(extentTest, "Click on \"" + text + "\"");
        safeClick(By.xpath("//" + type + "[contains(text(), '" + text + "')]"));
    }
}
