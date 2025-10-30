package pages.formation;

import base.PageTemplate;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.CustomFireFoxDriver;
import utils.Log;
import utils.PageUtils;

public class TrainingCoursePage extends PageTemplate {

    private final By newButton = By.xpath("//div[@title=\"Nouveau\"]");
    private final By saveButton = By.xpath("//button[@name=\"SaveEdit\"]");

    public TrainingCoursePage(CustomFireFoxDriver driver, ExtentTest extentTest) {
        super(driver, extentTest);
    }

    public void clickNewButton() {
        driver.safeClick(newButton);
        Log.extendedInfo(extentTest, "[Training Course] \"Nouveau\" button clicked.");
    }

    public void clickSaveButton() {
        driver.safeClick(saveButton);
        Log.extendedInfo(extentTest, "[Training Course] \"Save\" button clicked.");
    }

    public void selectTrainingCourseType(String type) {
        driver.findElement(By.xpath("//span[text()='" + type + "']/../..//span[@class='slds-radio--faux']")).click();
        driver.clickOnText(extentTest, "Suivant", null);
        Log.extendedInfo(extentTest, "Action type '" + type + "' selected.");
    }

    public void fillForm_ParcoursAcademies(String label, String startDate, String endDate) {
        Log.extendedInfo(extentTest, "Filling create form...");
        WebElement newForm = driver.findElement(By.xpath("//*[starts-with(@id,\"content_\")]/div/div/div"));

        WebElement nameField = newForm.findElement(By.name("Name"));
        nameField.clear();
        nameField.sendKeys(label);
        Log.extendedInfo(extentTest, "[Training Courses] Form - Label entered.");

        WebElement startDateField = newForm.findElement(By.xpath("//input[@name='DateDebutValidite__c']"));
        startDateField.clear();
        startDateField.sendKeys(startDate);

        WebElement endDateField = newForm.findElement(By.xpath("//input[@name='DateFinValidite__c']"));
        endDateField.clear();
        endDateField.sendKeys(endDate);
    }

    @Override
    public void navigateTo() throws InterruptedException {
        PageUtils.goToApplication(driver, extentTest, "Formation");
        PageUtils.goToHeaderPage(driver, extentTest, "Parcours de formation");
    }
}
