import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/java/FeatureFiles",
        glue = {"Util", "StepDefinitions"},
        plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        
        monochrome = true,
        publish = true)
public class TestNGTestRunner extends AbstractTestNGCucumberTests {

}
