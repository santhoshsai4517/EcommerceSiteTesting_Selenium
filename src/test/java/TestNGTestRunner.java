

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/java/FeatureFiles", glue = {"Utility","StepDefinitions"}  ,monochrome = true, tags = "@ErrorHandling")
public class TestNGTestRunner extends AbstractTestNGCucumberTests {

}
