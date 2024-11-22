package BaseTest;

import PageObjects.LoginPage;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;

public class BaseTest {

    public ChromeDriver driver;
    public LoginPage login;

    public ChromeDriver initializeDriver() throws FileNotFoundException, IOException {

        String downloadFilePath = "C:\\Users\\santh\\Desktop\\Ecommerce Automation\\Selenium\\EcommercePractise\\Downloads";
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{
            put("download.default_directory", downloadFilePath);
            put("download.prompt_for_download", false); // Disable the download prompt
            
        }});
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        return driver;
    }

    //	@BeforeMethod(alwaysRun = true)
    public LoginPage launchApplication() throws FileNotFoundException, IOException {
        driver = initializeDriver();
        login = new LoginPage(driver);
        login.goTo();
        return login;
    }


    public String getScreenshot(String testcaseName, WebDriver driver) throws IOException {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(src, new File(System.getProperty("user.dir") + "//reports//" + testcaseName + ".png"));
        return System.getProperty("user.dir") + "//reports//" + testcaseName + ".png";
    }


}
