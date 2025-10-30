# Virtuo IHM Test automation

## Run tests locally
### InteliJ Configuration
From InteliJ, create a "TestNG" configuration.
Set "Test kind" to "Suite" and specify the path to the **testng.xml** file located at the root of the project.
Running this configuration will launch all tests listed in the Suite.

You can also run a subset of tests grouped by "group".
Be careful: if a test depends on another test in a different group, you must also run that test, otherwise you may encounter an error.

**Example:** creating an event requires creating a location beforehand. If the event creation test _require_ the location creation test.
If you only run the event creation test, it will throw an error indicating it couldn't find the location creation test.

### Env File
Make sure to create a **.env** file at the root of your local project.
Use **.env-example** as a reference to avoid missing anything.
Obviously, **.env-example** only contains fake data.

### Program execution order
1. Create the "test report" object.
2. Run the tests
    1. Test initialization & platform login
    2. Execute test scenario actions & log actions
    3. Verify the test
    4. Attach a screenshot to the report in case of test failure
3. Generate the report in HTML format
4. Send the report by email

## Create a new test
### Test class
To create a new test case, simply add a new class in test/java/tests/*path*.
> Remember to add the class to the **testng.xml** file

This class must inherit from the BaseTest class.
BaseTest contains all the methods to be executed before and after a test runs.
You don't need to trigger anything manually, everything is automatic thanks to the **@BeforeMethod** and **@AfterMethod** annotations.

To create a test, add a method in this class with the **@Test** annotation.
To sort tests in the report, add the argument (groups = {"something"}) to this annotation.

### Log
To make these tests as useful as possible in case of failure, log as many actions as possible using the utility class Log, which contains methods such as info(message), warn(message), etc.

To log to the console or log file, use the methods info, warn, error... If you want the log to also appear in the test report, use extendedInfo, extendedWarn, extendedError, etc.

### Test structure
Tests are traditionally divided into 3 parts: Init, Actions, Asserts (Call them whatever you want, Given - When - Then...)

In Init, create the new test with `extentTest = ExtentReportManager.createTest("Create new plan");`.

Whenever possible, actions should be placed in methods contained within "Pages" objects. Pages class, represent a physical page of a website. See [pages infos heres](#Pages).

*Addendum: simply take inspiration from existing tests, feel free to improve them!*

## Project structure
Simplified project structure:
```
/project
|- src/
|	|
|	|- base/
|	|	|- BaseTest.java
|	|	|- PageTemplate.java
|	|
|	|- pages/
|	|	|- Group1/
|	|	|	|- Plans.java
|	|	|	|- Actions.java
|	|	|	|- ...
|	|	|- ...
|	|
|	|- utils/
|	|	|- Log.java
|	|	|- EmailUtils.java
|	|	|- ...
|
|- test/java/tests/group1/
|	|- ActionsTest.java
|	|- PlansTest.java
|	|- ...
|
|- output/
|	|- logs/
|	|- reports/
|	|- screenshots/
|
```
### Pages
Page objects are classes representing a web page. They contain WebElements and methods to interact with them (clickOnThisButton, writeInTheField...).
These **Page** classes all inherit from **PageTemplate**. Mind to implement navigateTo() method, to go to this page.
PageTemplate contains also a custom WebDriver and all needed objects during interactions.

### Utilities & tools
There are two types of utilities in the project.
Utilities for developers to help create test cases, and framework-specific utilities.
The second category includes, for example, the class for managing email sending or generating test reports.
These utilities are not meant to be used in many places and should not evolve frequently.

To simplify the work of developers, there are the SafeClick method on the custom WebDriver. This method tests different types of clicks (JavaScript events, physical clicks, etc.) to minimize errors.
A build-in clickOnText and clickOnPartialText methods to click on an element with text or partial text.

When a test fail or raise an error, a screenshot is taken and attached to the report.

### Output folder
The output folder, as its name suggests, contains all elements produced by and for generating the test report. (Similar to *target* in Maven)
It is not tracked by git, so it's normal if you don't see it until you've run the project at least once.

## Points of attention
- If you're on SalesForce, depending on environment settings, SalesForce may request a verification code sent by email during login.
  The system is not designed to retrieve the code from the email.
  A temporary error handler stops the tests and logs a specific error message.
  It is possible to disable this check, ask an administrator.
- On SalesForce: If a click doesn't work, sometimes clicking the parent element solves the issue.
- On SalesForce: Some elements don't exist when the page loads and are created when another element is clicked (e.g. dropdown menu, textarea, etc.) or when you scroll down.
- Remember to add new classes to the **testng.xml** file.
- Log as much as possible in test methods! (Use the Log class in utils/)
- Try to assign a group to each test and be mindful of test dependencies.
  (For example, test the creation of an element before testing its modification)
- Use the driver's **safeClick()** method instead of the WebElement's **click()** method.

## Addendum
Have a good day, and avoid throwing your computer out the window when Selenium can't find an element that's clearly right in front of you... I've already tried that, it doesn't solve the problem. Regards.

PS: It might just be because the element doesn't exist yet and requires a click on another element to be created; this happens often.