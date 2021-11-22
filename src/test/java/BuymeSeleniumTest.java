import com.github.multumit.buyme.SearchMenuDropdown;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;


import java.io.File;
import java.io.IOException;
import java.util.List;

public class BuymeSeleniumTest extends SeleniumTest {
    private static final String USERNAME = "y_walter@gmail.com";
    private static final String PASSWORD = "xxxxx_TOP_SECRET_xxxxx";

    private static final String NAME = "yoni";

    public static final int ACTION_WAIT_TIME = 500;

    @BeforeClass
    protected void gotoWebsite() throws InterruptedException {
        goToMainPage();
    }

    private void goToMainPage() throws InterruptedException {
        driver.get("https://buyme.co.il");
        Thread.sleep(ACTION_WAIT_TIME * 3);
    }

    @Test(priority = 1)
    public void testRegister() throws InterruptedException {
        WebElement loginButton = driver.findElement(By.className("seperator-link"));
        loginButton.click();
        Thread.sleep(ACTION_WAIT_TIME);

        // Show Register
        WebElement registerOrLogin = driver.findElement(By.className("register-or-login"));
        WebElement goToRegister = registerOrLogin.findElement(By.tagName("span"));
        goToRegister.click();
        Thread.sleep(ACTION_WAIT_TIME);

        // Fill Register
        fillRegisterField("input[placeholder=\"שם פרטי\"]", NAME);
        fillRegisterField("input[type='email']", USERNAME);
        fillRegisterField("input[placeholder=\"סיסמה\"]", PASSWORD);
        fillRegisterField("input[placeholder=\"אימות סיסמה\"]",PASSWORD);

        //subscription
        WebElement subscription = driver.findElement(By.cssSelector("button[gtm=\"הרשמה ל-BUYME\"]"));
        subscription.click();

        Thread.sleep(ACTION_WAIT_TIME);
        WebElement loginError = driver.findElement(By.className("login-error"));
        String loginErrorText = loginError.getText();

        Assert.assertEquals(loginErrorText, "הסיסמה צריכה להכיל לפחות 8 תווים, ביניהם לפחות אות אחת גדולה באנגלית, לפחות אות אחת קטנה באנגלית ולפחות ספרה אחת");

        takeScreenshot("./register_test_result.jpg");

    }

    private void takeScreenshot(String destFile) {
        TakesScreenshot scrShot =((TakesScreenshot)driver);
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        File DestFile=new File(destFile);
        try {
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (IOException e) {
            System.out.printf("failed writing: %s", e);
        }
    }

    private void fillRegisterField(String cssSelector, String value) throws InterruptedException {
        WebElement nameInput = driver.findElement(By.cssSelector(cssSelector));
        nameInput.click();
        Thread.sleep(ACTION_WAIT_TIME);
        nameInput.sendKeys(value);
    }

    @Test(priority = 2, enabled = false) // TODO: if you use an existing account enable this
    public void loginTest() throws InterruptedException { // Login existing account
        goToMainPage();

        WebElement loginButton = driver.findElement(By.className("seperator-link"));
        loginButton.click();
        Thread.sleep(ACTION_WAIT_TIME);

        // Fill credentials
        fillRegisterField("input[type='email']", USERNAME);
        fillRegisterField("input[placeholder=\"סיסמה\"]", PASSWORD);

        // Login
        WebElement subscription = driver.findElement(By.cssSelector("button[gtm=\"כניסה ל-BUYME\"]"));
        subscription.click();
        Thread.sleep(2 * ACTION_WAIT_TIME);
    }

    @Test(priority = 3 /*, dependsOnMethods = {"loginTest"} */)
    public void testHomeScreen() throws InterruptedException {
        this.goToMainPage();

        List<WebElement> forms = driver.findElements(By.tagName("form"));
        Assert.assertNotEquals(forms.size(), 0);

        WebElement menu = null;
        for (WebElement form : forms) {
            String formText = form.getText();
            if (formText.contains("סכום") && formText.contains("אזור") && formText.contains("קטגוריה")) {
                menu = form;
                break;
            }
        }

        Assert.assertNotNull(menu);
        // Get class with prefix chosen-container
        List<WebElement> listContainers = menu.findElements(By.cssSelector("div[class^='chosen-container']"));

        Actions actions = new Actions(driver);

        WebElement priceMenuElement = listContainers.get(0);
        SearchMenuDropdown price = new SearchMenuDropdown(priceMenuElement);
        price.makeMenuSelection(actions, ACTION_WAIT_TIME, 2);
        Assert.assertTrue(priceMenuElement.getText().contains("100"), String.format("unexpected result: %s", priceMenuElement.getText()));

        WebElement areaMenuElement = listContainers.get(1);
        SearchMenuDropdown area = new SearchMenuDropdown(areaMenuElement);
        area.makeMenuSelection(actions, ACTION_WAIT_TIME, 3);
        Assert.assertTrue(areaMenuElement.getText().contains("צפון"), String.format("unexpected result: %s", areaMenuElement.getText()));

        WebElement categoryMenuElement = listContainers.get(2);
        SearchMenuDropdown category = new SearchMenuDropdown(categoryMenuElement);
        category.makeMenuSelection(actions, ACTION_WAIT_TIME, 4);
        Assert.assertTrue(categoryMenuElement.getText().contains("מסעדות שף"), String.format("unexpected result: %s", categoryMenuElement.getText()));

        WebElement formSubmitLink = menu.findElement(By.cssSelector("a[class*='search']"));
        actions.moveToElement(formSubmitLink).click().perform();

        Thread.sleep(2 * ACTION_WAIT_TIME);
    }

    @Test(dependsOnMethods = "testHomeScreen")
    public void testPickBusiness() throws InterruptedException {
        String currentUrl = driver.getCurrentUrl();
        String expectedUrl = "https://buyme.co.il/search?budget=2&category=16&region=9";
        Assert.assertEquals(currentUrl, expectedUrl);

        List<WebElement> businesses = driver.findElements(By.cssSelector("ul[class*='bm-product-cards'] > li > a"));
        Assert.assertNotEquals(businesses.size(), 0);

        WebElement someBusiness = businesses.get(0);
        System.out.println(someBusiness.getText());
        Actions actions = new Actions(driver);
        actions.moveToElement(someBusiness).click().perform();

        Thread.sleep(ACTION_WAIT_TIME);

        WebElement inputPrice = driver.findElement(By.cssSelector("div[class*='money-input'] > * input"));
        actions.moveToElement(inputPrice).click().sendKeys("55").perform();

        Thread.sleep(ACTION_WAIT_TIME);

        WebElement submitPrice = driver.findElement(By.cssSelector("div[class*='money-btn'] > button"));
        submitPrice.click();

        Thread.sleep(ACTION_WAIT_TIME);
    }
}

