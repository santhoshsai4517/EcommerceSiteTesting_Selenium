package BaseTest;

import PageObjects.LoginPage;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
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

import static org.hamcrest.Matchers.lessThan;

public class BaseTest {

    public ChromeDriver driver;
    public LoginPage login;
    public int responseTime = 4000;
    public String country = "India";

    public RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/api/ecom")
            .setContentType(ContentType.JSON).build();

    public ResponseSpecification getResponseSpecification(int statusCode, long responseTime, ContentType contentType) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .expectContentType(contentType)
                .expectResponseTime(lessThan(responseTime)) // Validate response time is less than 1000 ms
                .expectHeader("Server", "Apache/2.4.52 (Ubuntu)") // Validate the Server header
                .build();
    }

    public ChromeDriver initializeDriver() {

        String downloadFilePath = "Downloads";
        // Retrieve the 'headless' option from the CLI or environment
        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless"));
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{
            put("download.default_directory", downloadFilePath);
            put("download.prompt_for_download", false); // Disable the download prompt

        }});
        options.addArguments("--remote-allow-origins=*");
        // Add the headless argument if the 'headless' option is true
        if (isHeadless) {
            options.addArguments("--headless");
        }
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
