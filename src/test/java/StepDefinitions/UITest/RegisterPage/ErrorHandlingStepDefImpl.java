package StepDefinitions.UITest.RegisterPage;

import BaseTest.BaseTest;
import PageObjects.LoginPage;
import PageObjects.RegisterPage;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ErrorHandlingStepDefImpl extends BaseTest {

    private LoginPage login;
    private RegisterPage register;

    @Given("User landed on ECommerece registration page")
    public void user_landed_on_e_commerece_registration_page() throws FileNotFoundException, IOException {
        register = launchApplication().goToRegisterPage();
    }

    @When("User submits empty registration form")
    public void user_submits_empty_registration_form() {
        register.clickRegisterButton();
    }

    @When("^Incorrect details (.+) (.+) (.+) (.+) (.+) (.+) (.+) (.+) (.+) and error occurs$")
    public void incorrect_details_and_error_occurs(String fName, String lName, String email, String mobile,
                                                   String occupation, String gender, String password, String confirmPassword, boolean check) {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), request ->

        {
            Request req = request.getRequest();
            if (req.getUrl().contains("register")) {
                RequestId requestId = request.getRequestId();
                String requestPayload = devTools.send(Network.getRequestPostData(requestId));

                JSONObject jsonObject = new JSONObject(requestPayload);

                Assert.assertEquals(jsonObject.getString("firstName"), fName);
                Assert.assertEquals(jsonObject.getString("lastName"), lName);
                Assert.assertEquals(jsonObject.getString("userEmail"), email);
                Assert.assertEquals(jsonObject.getString("userMobile"), mobile);
                Assert.assertEquals(jsonObject.getString("userRole"), "customer");
                Assert.assertEquals(jsonObject.getString("occupation"), occupation);
                Assert.assertEquals(jsonObject.getString("gender"), gender);
                Assert.assertEquals(jsonObject.getString("userPassword"), password);
                Assert.assertEquals(jsonObject.getString("confirmPassword"), confirmPassword);
                Assert.assertEquals(jsonObject.getBoolean("required"), check);
            }

        });

        register.registerUser(fName, lName, email, mobile, occupation, gender, password, confirmPassword, check);
    }

    @Then("{string} {string} {string} {string} {string} {string} message is displayed")
    public void message_is_displayed(String fNameError, String emailError, String mobileError, String passwordError,
                                     String confirmPasswordError, String checkBoxError) {
        Assert.assertEquals(register.getfirstNameError(), fNameError);
        Assert.assertEquals(register.getEmailError(), emailError);
        Assert.assertEquals(register.getPhoneNumbnerError(), mobileError);
        Assert.assertEquals(register.getPasswordError(), passwordError);
        Assert.assertEquals(register.getConfirmPasswordError(), confirmPasswordError);
        Assert.assertEquals(register.getCheckBoxError(), checkBoxError);
    }

    @Then("{string} password mismatch error message is displayed")
    public void password_mismatch_error_message_is_displayed(String error) {
        Assert.assertEquals(register.getPasswordMismatchError(), error);
    }

    @Then("{string} invalid email error message is displayed")
    public void invalid_email_error_message_is_displayed(String error) {
        Assert.assertEquals(register.getInvalidEmaiLError(), error);
    }

    @Then("{string} mobile error message is displayed")
    public void mobile_error_message_is_displayed(String error) {
        Assert.assertEquals(register.getPhoneNumberWithCharsError(), error);
    }

    @Then("{string} 1o digit mobile error message is displayed")
    public void digit_mobile_error_message_is_displayed(String error) {
        Assert.assertEquals(register.getPhoneNumber10DigitsError(), error);
    }

    @Then("{string} USER EXIST error message is displayed")
    public void user_exist_error_message_is_displayed(String error) {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.responseReceived(), response -> {
            Response res = response.getResponse();
            if (res.getUrl().contains("register")) {
                RequestId requestId = response.getRequestId();
                String responsePayload = devTools.send(Network.getResponseBody(requestId)).toString();
                JSONObject jsonObject = new JSONObject(responsePayload);
                Assert.assertEquals(jsonObject.getString("message"), "User already exisits with this Email Id!");
            }

        });
        Assert.assertEquals(register.getUserAlreadyExistsError(), error);
    }

    @Then("^(.+) password length error message is displayed$")
    public void password_length_error_message_is_displayed(String error) {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.responseReceived(), response -> {
            Response res = response.getResponse();
            if (res.getUrl().contains("register")) {
                RequestId requestId = response.getRequestId();
                String responsePayload = devTools.send(Network.getResponseBody(requestId)).toString();
                JSONObject jsonObject = new JSONObject(responsePayload);
                Assert.assertEquals(jsonObject.getString("error"), error);
            }

        });
        Assert.assertEquals(register.getPasswordErrorToast(), error);
    }

    @When("^API Intercepted and failed currect details (.+) (.+) (.+) (.+) (.+) (.+) (.+) (.+) (.+) and error occurs$")
    public void api_intercepted_and_failed_currect_details_and_error_occurs(String fName, String lName, String email,
                                                                            String mobile, String occupation, String gender, String password, String confirmPassword, boolean check) {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        Optional<List<RequestPattern>> patterns = Optional
                .of(Arrays.asList(new RequestPattern(Optional.of("*register*"), Optional.empty(), Optional.empty())));

        devTools.send(Fetch.enable(patterns, Optional.empty()));

        devTools.addListener(Fetch.requestPaused(), request -> {
            System.out.println("dd");
            devTools.send(Fetch.failRequest(request.getRequestId(), ErrorReason.FAILED));

        });

        register.registerUser(fName, lName, email, mobile, occupation, gender, password, confirmPassword, check);
    }

    @When("^API is intercepted and payload is modified and correct details (.+) (.+) (.+) (.+) (.+) (.+) (.+) (.+) (.+) and error occurs$")
    public void API_is_intercepted_and_payload_is_modified_and_correct_details_and_error_occurs(String fName,
                                                                                                String lName, String email, String mobile, String occupation, String gender, String password,
                                                                                                String confirmPassword, boolean check) {

        DevTools devTools = driver.getDevTools();
        devTools.createSession();

        Optional<List<RequestPattern>> patterns = Optional.of(
                Arrays.asList(new RequestPattern(Optional.of("*register*"), Optional.empty(), Optional.empty())));

        devTools.send(Fetch.enable(patterns, Optional.empty()));

        devTools.addListener(Fetch.requestPaused(), requestPaused -> {
            org.openqa.selenium.devtools.v127.fetch.model.RequestId requestId = requestPaused.getRequestId();
            System.out.println(requestId);
            String originalBody = requestPaused.getRequest().getPostData().orElse("");
            originalBody = originalBody.replace(email, "santhoshsai4517@gmail.com");
            System.out.println(originalBody);
            devTools.send(Fetch.continueRequest(requestId, Optional.empty(), Optional.empty(),
                    Optional.of(originalBody), Optional.empty(), Optional.empty()));
//	            String requestPayload = devTools.send(Network.getRequestPostData(requestId));
        });

        devTools.addListener(Network.responseReceived(), response -> {
            Response res = response.getResponse();
            if (res.getUrl().contains("register")) {
                RequestId requestId = response.getRequestId();
                String responsePayload = devTools.send(Network.getResponseBody(requestId)).toString();
                JSONObject jsonObject = new JSONObject(responsePayload);
                Assert.assertEquals(jsonObject.getString("message"), "User already exisits with this Email Id!");
            }

        });
        register.registerUser(fName, lName, email, mobile, occupation, gender, password, confirmPassword, check);
    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.close();
    }
}
