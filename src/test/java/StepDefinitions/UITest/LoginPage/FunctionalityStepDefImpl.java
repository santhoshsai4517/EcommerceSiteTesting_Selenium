package StepDefinitions.UITest.LoginPage;

import BaseTest.BaseTest;
import PageObjects.ForgotPasswordPage;
import PageObjects.ProductsPage;
import PageObjects.RegisterPage;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v130.network.Network;
import org.openqa.selenium.devtools.v130.network.model.Request;
import org.openqa.selenium.devtools.v130.network.model.RequestId;
import org.testng.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class FunctionalityStepDefImpl extends BaseTest {

    private RegisterPage register;
    private ForgotPasswordPage forgotpassword;
    private ProductsPage productspage;

    @Given("I landed on ECommerece page")
    public void I_landed_on_ECommerece_page() throws FileNotFoundException, IOException {
        launchApplication();
    }

    @Then("{string} message is displayed")
    public void message_is_displayed_on_confiramtion_page(String message) {
        Assert.assertEquals(login.getLoginPageText(), message);
    }

    @When("I Click on Register button")
    public void i_click_on_register_button() {
        register = login.goToRegisterPage();
    }

    @When("I Click on Register link")
    public void i_click_on_register_link() {
        register = login.goToRegisterPageLink();
    }

    @Then("Page is redirected to register page and  {string} text is displayed")
    public void page_is_redirected_to_register_page_and_text_is_displayed(String message) {
        Assert.assertEquals(register.getRegisterPageText(), message);
    }

    @When("I Click on Forgot password link")
    public void i_click_on_forgot_password_link() {
        forgotpassword = login.goToForgotPassword();
    }

    @Then("Page is redirected to forgot password page and  {string} text is displayed")
    public void page_is_redirected_to_forgot_password_page_and_text_is_displayed(String message) {
        Assert.assertEquals(forgotpassword.getTitleText(), message);
    }

    @When("^Logged in with username (.+) and password (.+)$")
    public void logged_in_with_username_and_password(String username, String password) {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), request ->

        {
            Request req = request.getRequest();
            if (req.getUrl().contains("auth")) {
                RequestId requestId = request.getRequestId();
                String requestPayload = devTools.send(Network.getRequestPostData(requestId));
                System.out.println(requestPayload);
                JSONObject jsonObject = new JSONObject(requestPayload);
                Assert.assertEquals(jsonObject.getString("userEmail"), username);
                Assert.assertEquals(jsonObject.getString("userPaassword"), password);
            }


        });

        productspage = login.loginApplication(username, password);
    }

    @Then("{string} message is displayed and {string} title is visible")
    public void message_is_displayed_and_title_is_visible(String message, String message2) {
        Assert.assertEquals(login.getSuccessText(), message);
        Assert.assertEquals(productspage.getTitleText(), message2);


    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.close();
    }


}
