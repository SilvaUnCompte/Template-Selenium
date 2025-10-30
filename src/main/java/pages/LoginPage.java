package pages;

import base.PageTemplate;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.CustomFireFoxDriver;
import utils.Log;

public class LoginPage extends PageTemplate {

    @FindBy(id = "username")
    WebElement usernameField;

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy(id = "Login")
    WebElement loginButton;

    public LoginPage(CustomFireFoxDriver driver, ExtentTest extentTest) {
        super(driver, extentTest);
    }

    public void enterUsername(String username) {
        usernameField.clear();
        usernameField.sendKeys(username);
        Log.extendedInfo(extentTest, "[LoginPage] Username typed.");
    }

    public void enterPassword(String password) {
        passwordField.clear();
        passwordField.sendKeys(password);
        Log.extendedInfo(extentTest, "[LoginPage] Password typed.");
    }

    public void clickLogin() {
        loginButton.click();
        Log.extendedInfo(extentTest, "[LoginPage] Login button clicked.");
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    @Override
    public void navigateTo() {
        driver.navigate().to(dotenv.get("LOGIN_URL"));
    }
}
