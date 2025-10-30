package base;

import com.aventstack.extentreports.ExtentTest;
import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import utils.CustomFireFoxDriver;

public abstract class PageTemplate {
    protected final Dotenv dotenv = Dotenv.load();
    protected final ExtentTest extentTest;
    protected final CustomFireFoxDriver driver;
    protected final Actions actions;

    protected PageTemplate(CustomFireFoxDriver driver, ExtentTest extentTest) {
        this.driver = driver;
        this.extentTest = extentTest;
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    abstract public void navigateTo() throws InterruptedException;
}
