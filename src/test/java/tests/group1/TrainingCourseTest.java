package tests.group1;

import base.BaseTest;
import exeption.AuthCodeExeption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.formation.TrainingCoursePage;
import utils.ExtentReportManager;
import utils.Log;
import utils.PageUtils;

import java.util.concurrent.ThreadLocalRandom;

public class TrainingCourseTest extends BaseTest {

    private final int randomNum = ThreadLocalRandom.current().nextInt(2000, 3000);
    private final int randomNum2 = ThreadLocalRandom.current().nextInt(3000, 4000);

    @Test(groups = {"TrainingCourse"})
    public void createNewTraningCourse() throws AuthCodeExeption, InterruptedException {
        // Init test
        extentTest = ExtentReportManager.createTest("Create new TraningCourse (Parcours Académies)");
        PageUtils.Login(dotenv.get("VIRTUO_USER"), dotenv.get("VIRTUO_PASSWORD"), driver, extentTest);
        TrainingCoursePage page = new TrainingCoursePage(driver, extentTest);
        page.navigateTo();

        // Actions
        page.clickNewButton();
        page.selectTrainingCourseType("Parcours académies");
        page.fillForm_ParcoursAcademies("test_" + randomNum + randomNum2, "11/04/" + randomNum, "11/09/" + randomNum2);
        page.clickSaveButton();

        // Asserts
        Log.extendedInfo(extentTest, "Verifying creation...");
        Assert.assertFalse(driver.findElements(By.xpath("//*[contains(text(), 'test_" + randomNum + randomNum2 + "')]")).isEmpty());
        Assert.assertFalse(driver.findElements(By.xpath("//*[contains(text(), '11/09/" + randomNum2 + "')]")).isEmpty());
        Assert.assertFalse(driver.findElements(By.xpath("//*[contains(text(), '11/04/" + randomNum + "')]")).isEmpty());
    }


    @Test(groups = {"TrainingCourse"}, dependsOnMethods = {"createNewTraningCourse"})
    public void editTraningCourse() throws AuthCodeExeption, InterruptedException {
        // Init test
        extentTest = ExtentReportManager.createTest("Edit room");
        PageUtils.Login(dotenv.get("VIRTUO_USER"), dotenv.get("VIRTUO_PASSWORD"), driver, extentTest);
        TrainingCoursePage page = new TrainingCoursePage(driver, extentTest);
        page.navigateTo();

        // Actions
        driver.clickOnText(extentTest, "Modification", null);
        Log.extendedInfo(extentTest, "Pen edit button clicked.");

        WebElement nameField = driver.findElement(By.name("Name"));
        nameField.clear();
        nameField.sendKeys("test_" + randomNum + "_edited");
        Log.extendedInfo(extentTest, "Label edited.");

        page.clickSaveButton();

        // Asserts
        Thread.sleep(1000);
        Log.extendedInfo(extentTest, "Verifying edition...");
        Assert.assertFalse(driver.findElements(By.xpath("//*[contains(text(), 'test_" + randomNum + "_edited')]")).isEmpty());
    }
}
