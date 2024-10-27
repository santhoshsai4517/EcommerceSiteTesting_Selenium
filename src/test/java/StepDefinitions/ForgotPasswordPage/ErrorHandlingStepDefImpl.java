package StepDefinitions.ForgotPasswordPage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v126.network.Network;
import org.openqa.selenium.devtools.v126.network.model.Request;
import org.openqa.selenium.devtools.v126.network.model.RequestId;
import org.openqa.selenium.devtools.v126.network.model.Response;
import org.openqa.selenium.devtools.v127.fetch.Fetch;
import org.openqa.selenium.devtools.v127.fetch.model.RequestPattern;
import org.openqa.selenium.devtools.v127.network.model.ErrorReason;
import org.testng.Assert;

import BaseTest.BaseTest;
import PageObjects.ForgotPasswordPage;
import PageObjects.LoginPage;
import PageObjects.ProductsPage;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ErrorHandlingStepDefImpl extends BaseTest {

	private LoginPage login;
	private ForgotPasswordPage forgot;

	@Given("User landed on ECommerece page and forgot password page")
	public void user_landed_on_e_commerece_page_and_forgot_password_page() throws FileNotFoundException, IOException {
		login = launchApplication();
		forgot = login.goToForgotPassword();
	}

	@When("User submits empty form")
	public void user_submits_empty_form() {
		forgot.updatePassword("", "", "");
	}

	@Then("{string} {string} {string} Errors are displayed")
	public void errors_are_displayed(String error, String error1, String error2) {
		Assert.assertEquals(forgot.getEmailError(), error);
		Assert.assertEquals(forgot.getPasswordError(), error1);
		Assert.assertEquals(forgot.getConfirmPasswordErrpr(), error2);
		Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/password-new");
	}

	@When("^User provied invalid (.+) (.+) (.+) and submits the form$")
	public void user_provied_invalid_details_and_submits_the_form(String email, String password,
			String confirmPassword) {
		forgot.updatePassword(email, password, confirmPassword);
	}

	@Then("{string} email error message is displayed")
	public void email_error_message_is_displayed(String error) {
		Assert.assertEquals(forgot.getInvalidEmailError(), error);
		Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/password-new");
	}

	@When("^User provied (.+) and different passwords (.+) (.+) and submits the form$")
	public void user_provied_different_password_and_submits_the_form(String email, String password,
			String confirmPassword) {
		forgot.updatePassword(email, password, confirmPassword);
	}

	@Then("{string} password error message is displayed")
	public void password_error_message_is_displayed(String error) {
		Assert.assertEquals(forgot.getPasswordMismatchError(), error);
		Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/password-new");
	}

	@When("^User provied not registered email (.+) (.+) (.+) and submits the form$")
	public void user_provied_not_recognised_mail_and_submits_the_form(String email, String password,
			String confirmPassword) {
		DevTools devTools = driver.getDevTools();
		devTools.createSession();
		devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
		devTools.addListener(Network.requestWillBeSent(), request ->

		{
			Request req = request.getRequest();
			if (req.getUrl().contains("new-password")) {
				RequestId requestId = request.getRequestId();
				String requestPayload = devTools.send(Network.getRequestPostData(requestId));
				System.out.println(requestPayload);
				JSONObject jsonObject = new JSONObject(requestPayload);
				Assert.assertEquals(jsonObject.getString("userEmail"), email);
				Assert.assertEquals(jsonObject.getString("userPaassword"), password);
				Assert.assertEquals(jsonObject.getString("confirmPassword"), confirmPassword);
			}

		});

		devTools.addListener(Network.responseReceived(), response -> {
			Response res = response.getResponse();
			if (res.getUrl().contains("new-password")) {
				RequestId requestId = response.getRequestId();
				String responsePayload = devTools.send(Network.getResponseBody(requestId)).toString();
				JSONObject jsonObject = new JSONObject(responsePayload);
				Assert.assertEquals(jsonObject.getString("message"), "User Not found.");
			}

		});
		forgot.updatePassword(email, password, confirmPassword);
	}

	@Then("{string} user error message is displayed")
	public void user_error_message_is_displayed(String error) {
		Assert.assertEquals(forgot.getUserNotFoundError(), error);
		Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/password-new");
	}

	@When("^API is down and User provied (.+) and (.+) (.+)  and submits the form$")
	public void api_is_down_and_user_provied_and_submits_the_form(String email, String password,
			String confirmPassword) {

		DevTools devTools = driver.getDevTools();
		devTools.createSession();
		Optional<List<RequestPattern>> patterns = Optional.of(
				Arrays.asList(new RequestPattern(Optional.of("*new-password*"), Optional.empty(), Optional.empty())));

		devTools.send(Fetch.enable(patterns, Optional.empty()));

		devTools.addListener(Fetch.requestPaused(), request -> {
			System.out.println("dd");
			devTools.send(Fetch.failRequest(request.getRequestId(), ErrorReason.FAILED));

		});
		forgot.updatePassword(email, password, confirmPassword);
	}

	@Then("{string} api error message is displayed")
	public void api_error_message_is_displayed(String error) {
		Assert.assertEquals(forgot.getUnknownError(), error);
		Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/password-new");
	}

	@When("^User provied correct (.+) (.+) (.+) and submits the form$")
	public void user_provied_correct_details_and_submits_the_form(String email, String password,
			String confirmPassword) {

		DevTools devTools = driver.getDevTools();
		devTools.createSession();

		Optional<List<RequestPattern>> patterns = Optional
				.of(Arrays.asList(new RequestPattern(Optional.of("*new-password*"), Optional.empty(), Optional.empty())));

		devTools.send(Fetch.enable(patterns, Optional.empty()));

		devTools.addListener(Fetch.requestPaused(), requestPaused -> {
			org.openqa.selenium.devtools.v127.fetch.model.RequestId requestId = requestPaused.getRequestId();
			System.out.println(requestId);
			String originalBody = requestPaused.getRequest().getPostData().orElse("");
			originalBody = originalBody.replace(email, "123456");
			System.out.println(originalBody);
			devTools.send(Fetch.continueRequest(requestId, Optional.empty(), Optional.empty(),
					Optional.of(originalBody), Optional.empty(), Optional.empty()));
//	            String requestPayload = devTools.send(Network.getRequestPostData(requestId));
		});

		devTools.addListener(Network.responseReceived(), response -> {
			Response res = response.getResponse();
			if (res.getUrl().contains("new-password")) {
				RequestId requestId = response.getRequestId();
				String responsePayload = devTools.send(Network.getResponseBody(requestId)).toString();
				JSONObject jsonObject = new JSONObject(responsePayload);
				Assert.assertEquals(jsonObject.getString("message"), "User Not found.");
			}

		});
		forgot.updatePassword(email, password, confirmPassword);
	}

	@After
	public void afterScenario() {
		if (driver != null)
			driver.close();
	}

}
