import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class SeleniumTest {
    protected static WebDriver driver;

    @BeforeClass
    protected static void initializeDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\yustin walter\\Downloads\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @AfterClass
    protected static void tearDown(){
        driver.quit();
    }
}
