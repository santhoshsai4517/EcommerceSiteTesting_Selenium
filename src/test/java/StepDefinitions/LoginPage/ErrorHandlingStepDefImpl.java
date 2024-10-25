package StepDefinitions.LoginPage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v126.network.Network;
import org.openqa.selenium.devtools.v127.fetch.Fetch;
import org.openqa.selenium.devtools.v127.fetch.model.RequestId;
import org.openqa.selenium.devtools.v127.fetch.model.RequestPattern;
import org.openqa.selenium.devtools.v127.network.model.ErrorReason;
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

	@When("^Api call is failed Logged in with wrong username (.+) and password (.+)$")
	public void api_call_is_failed_logged_in_with_wrong_username_and_password(String username, String password) {
		DevTools devTools = driver.getDevTools();
		devTools.createSession();
		Optional<List<RequestPattern>> patterns = Optional
				.of(Arrays.asList(new RequestPattern(Optional.of("*auth*"), Optional.empty(), Optional.empty())));

		devTools.send(Fetch.enable(patterns, Optional.empty()));

		devTools.addListener(Fetch.requestPaused(), request -> {

			devTools.send(Fetch.failRequest(request.getRequestId(), ErrorReason.FAILED));

		});

		login.loginApplication(username, password);
	}
	
	@When("^API payload chnaged Logged in with wrong username (.+) and password (.+)$")
	public void api_payload_changed_logged_in_with_wrong_username_and_password(String username, String password) {
		DevTools devTools = driver.getDevTools();
		devTools.createSession();
		Optional<List<RequestPattern>> patterns = Optional
				.of(Arrays.asList(new RequestPattern(Optional.of("*auth*"), Optional.empty(), Optional.empty())));

		devTools.send(Fetch.enable(patterns, Optional.empty()));

		 devTools.addListener(Fetch.requestPaused(),requestPaused -> {
	            RequestId requestId = requestPaused.getRequestId();
	            System.out.println(requestId);
	            String originalBody = requestPaused.getRequest().getPostData().orElse("");
	            originalBody = originalBody.replace("151Fa04124@4517", "123456");
	            System.out.println(originalBody);
	            devTools.send(Fetch.continueRequest(
	                    requestId,
	                    Optional.empty(),
	                    Optional.empty(),	              
	                    Optional.of(originalBody),
	                    Optional.empty(),
	                    Optional.empty()
	            ));
//	            String requestPayload = devTools.send(Network.getRequestPostData(requestId));
	        });

		login.loginApplication(username, password);
	}
	
	@Then("{string} error message is displayed")
	public void error_message_is_displayed(String message) {
		Assert.assertEquals(login.getErrorText(), message);
	}
	
	@Then("{string} error message is not displayed")
	public void error_message_is_not_displayed(String message) {
		Assert.assertFalse(login.checkError() );
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
