import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(features = "src/test/java/FeatureFiles/UITest/OrdersPage/ErrorHandling.feature",
        glue = {"Util", "StepDefinitions"},
        plugin = {"pretty", "timeline:Reports", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        tags = "@ErrorHandling",
        monochrome = true,
        publish = true)
public class TestNGTestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
