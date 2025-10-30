package utils;

import com.aventstack.extentreports.ExtentTest;
import exeption.AuthCodeExeption;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import pages.LoginPage;

public class PageUtils {
    private PageUtils() {
    }

    // Note: It's an example for SalesForce
    public static void Login(String username, String password, CustomFireFoxDriver driver, ExtentTest extentTest) throws AuthCodeExeption {
        LoginPage loginPage = new LoginPage(driver, extentTest);
        loginPage.login(username, password);

        if (driver.findElements(By.className("uiImage")).isEmpty()) {
            if (!driver.findElements(By.xpath("//*[contains(text(), 'Vérifier votre identité')]")).isEmpty()) {
                String message = "Verification par mail du login. Test impossible.";
                Log.extendedError(extentTest, message, null);
                throw new AuthCodeExeption(message);
            } else {
                String message = "Error inconnu durant l'authentification. Test impossible.";
                Log.extendedError(extentTest, message, null);
                throw new AuthCodeExeption(message);
            }
        }
    }

    // Note: It's an example for SalesForce
    public static void goToApplication(CustomFireFoxDriver driver, ExtentTest extentTest, String applicationName) throws InterruptedException {
        int i = 0;
        while (driver.findElements(By.xpath("//span[@title=\"" + applicationName + "\" and text()=\"" + applicationName + "\"]")).isEmpty()) {

            if (i >= 2) {
                throw new TimeoutException();
            } else {
                i++;
            }

            Log.extendedInfo(extentTest, "[Navigator] Going to the '" + applicationName + "' home page...");
            driver.safeClick(By.xpath("//*[@title=\"Lanceur d'application\"]"));
            Log.extendedInfo(extentTest, "[Navigator] Clicked on navigator button");
            Thread.sleep(300); // Sometimes needed on Firefox
            WebElement searchField = driver.findElement(By.xpath("//*[@type=\"search\" and @class=\"slds-input\"]"));
            searchField.sendKeys(applicationName);
            searchField.sendKeys(Keys.ENTER); // Enter key need to be separated (sync problem on firefox)
            Log.extendedInfo(extentTest, "[Navigator] Clicked on '" + applicationName + "' button");
        }
    }

    // Note: It's an example for SalesForce
    public static void goToHeaderPage(CustomFireFoxDriver driver, ExtentTest extentTest, String label) {
        if (driver.findElement(By.xpath("//a[@title=\"" + label + "\"]/parent::*")).isDisplayed()) {
            driver.safeClick(By.xpath("//a[@title=\"" + label + "\"]/parent::*"));
        } else {
            driver.safeClick(By.xpath("//span[text()=\"Plus\"]/parent::*"));
            driver.safeClick(By.xpath("//a[@role=\"menuitem\"]//span[text()=\"" + label + "\"]/parent::*"));
        }
        Log.extendedInfo(extentTest, "[Navigator] Go to \"" + label + "\" page");
    }
}
