package StepDefinitions.UITest.ForgotPasswordPage;

import BaseTest.BaseTest;
import PageObjects.ForgotPasswordPage;
import PageObjects.LoginPage;
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

    private LoginPage login;
    private RegisterPage register;
    private ForgotPasswordPage forgot;

    @Given("User landed on ECommerece page forgot password page")
    public void user_landed_on_e_commerece_page_forgot_password_page() throws FileNotFoundException, IOException {
        login = launchApplication();
        forgot = login.goToForgotPassword();
    }

    @When("User clicks on register link")
    public void user_clicks_on_register_link() {
        register = forgot.clickRegisterLink();
    }

    @Then("{string} register page is displayed")
    public void register_page_is_displayed(String message) {
        Assert.assertEquals(register.getRegisterPageText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/register");
    }

    @When("User clicks on login link")
    public void user_clicks_on_login_link() {
        login = forgot.clickLoginLink();
    }

    @Then("{string} login page is displayed")
    public void login_page_is_displayed(String message) {
        Assert.assertEquals(login.getLoginPageText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/login");
    }

    @When("^User provied (.+) (.+) and (.+) and submits the form$")
    public void user_provied_details_and_submits_the_form(String email, String password, String confirmPassword) {
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
                Assert.assertEquals(jsonObject.getString("userPassword"), password);
                Assert.assertEquals(jsonObject.getString("confirmPassword"), confirmPassword);
            }

        });
        login = forgot.updatePassword(email, password, confirmPassword);
    }

    @Then("{string} message is displayed and {string} login page is displayed")
    public void message_is_displayed_and_login_page_is_displayed(String message, String message1)
            throws InterruptedException {
        Assert.assertEquals(login.getLoginPageText(), message1);
        Assert.assertEquals(login.getPasswordUpdateText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/login");
        Thread.sleep(3000);
        ProductsPage productspage = login.loginApplication("s1234@gmail.com", "123");
        Assert.assertEquals(login.getSuccessText(), "Login Successfully");
        Assert.assertEquals(productspage.getTitleText(), "AUTOMATION");
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/dash");

    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.quit();
    }

}
