import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/java/FeatureFiles/APITest", glue = {"Util", "StepDefinitions"}, tags = "@FunctionalAPI or @ErrorHandlingAPI", monochrome = true)
public class TestNGTestRunner extends AbstractTestNGCucumberTests {

}
