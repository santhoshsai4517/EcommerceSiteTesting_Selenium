import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/java/FeatureFiles", glue = {"Util", "StepDefinitions"}, tags = "@Functional or @ErrorHandling", monochrome = true)
public class TestNGTestRunner extends AbstractTestNGCucumberTests {

}
