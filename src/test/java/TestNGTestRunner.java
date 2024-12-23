import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/java/FeatureFiles", glue = {"Util", "StepDefinitions/APITest"}, tags = "@FunctionalAPI or @ErrorHandlingAPI", monochrome = true)
public class TestNGTestRunner extends AbstractTestNGCucumberTests {

}
