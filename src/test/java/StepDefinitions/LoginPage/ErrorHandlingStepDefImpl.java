package StepDefinitions.LoginPage;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.Assert;

import BaseTest.BaseTest;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ErrorHandlingStepDefImpl extends BaseTest {

	@Given("I landed on ECommerece page and logging in")
	public void I_landed_on_ECommerece_page_and_logging_in() throws FileNotFoundException, IOException {
		launchApplication();
	}

	@When("^Logged in with wrong username (.+) and password (.+)$")
	public void logged_in_with_wrong_username_and_password(String username, String password) {
		login.loginApplication(username, password);
	}

	@Then("{string} error message is displayed")
	public void error_message_is_displayed(String message) {
		Assert.assertEquals(login.getErrorText(), message);
	}
	
	@When("Logged in with no login details")
	public void logged_in_with_no_login_details() {
	   login.loginApplication("", "");
	}
	
	@Then("{string} and {string} error message is displayed")
	public void error_message_is_displayed(String message,String message1) {
		Assert.assertEquals(login.getEmailErrorText(), message);
		Assert.assertEquals(login.getPasswordErrorText(), message1);
	}
	
	@Then("{string} invalid message is displayed")
	public void invalid_message_is_displayed(String message) {
		Assert.assertEquals(login.getEmailErrorText(), message);
	}
	
	

	@After
	public void afterScenario() {
		if (driver != null)
			driver.close();
	}

}
