package StepDefinitions.UITest.RegisterPage;

import BaseTest.BaseTest;
import PageObjects.LoginPage;
import PageObjects.ProductsPage;
import PageObjects.RegisterPage;
import com.github.javafaker.Faker;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONObject;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v126.network.Network;
import org.openqa.selenium.devtools.v126.network.model.Request;
import org.openqa.selenium.devtools.v126.network.model.RequestId;
import org.testng.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class FunctionalityStepDefImpl extends BaseTest {

    private LoginPage login;
    private RegisterPage register;
    private String msg;
    private ProductsPage prod;
    private String email, password;

    @Given("User landed on ECommerece register page")
    public void user_landed_on_e_commerece_register_page() throws FileNotFoundException, IOException {
        register = launchApplication().goToRegisterPage();
    }

    @When("User click on login link")
    public void user_click_on_login_link() {
        login = register.gotologinPage();
    }

    @Then("{string} login message is displayed")
    public void message_is_displayed(String message) {
        Assert.assertEquals(login.getLoginPageText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/login");
    }

    @When("^User provied (.+) (.+) (.+) (.+) (.+) (.+) (.+) (.+) (.+) and submits form with correct details$")
    public void user_provied_correct_details(String fName, String lName, String email, String mobile, String occupation,
                                             String gender, String password, String confirmPassword, boolean check) {

        Faker faker = new Faker();
        this.email = faker.internet().emailAddress();
        this.password = password;
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), request ->

        {
            Request req = request.getRequest();
            if (req.getUrl().contains("register")) {
                RequestId requestId = request.getRequestId();
                String requestPayload = devTools.send(Network.getRequestPostData(requestId));
                System.out.println(requestPayload);
                JSONObject jsonObject = new JSONObject(requestPayload);

                Assert.assertEquals(jsonObject.getString("firstName"), fName);
                Assert.assertEquals(jsonObject.getString("lastName"), lName);
                Assert.assertEquals(jsonObject.getString("userEmail"), this.email);
                Assert.assertEquals(jsonObject.getString("userMobile"), mobile);
                Assert.assertEquals(jsonObject.getString("userRole"), "customer");
                Assert.assertEquals(jsonObject.getString("occupation"), occupation);
                Assert.assertEquals(jsonObject.getString("gender"), gender);
                Assert.assertEquals(jsonObject.getString("userPassword"), this.password);
                Assert.assertEquals(jsonObject.getString("confirmPassword"), confirmPassword);
                Assert.assertEquals(jsonObject.getBoolean("required"), check);
            }

        });
        register.registerUser(fName, lName, this.email, mobile, occupation, gender, this.password, confirmPassword, check);
        msg = register.getSuccessMessage();
    }

    @Then("{string} accoun created message is displayed")
    public void accoun_created_message_is_displayed(String message) throws InterruptedException {
        Assert.assertEquals(msg, message);
        Thread.sleep(3000);
        login = register.gotoLoginAfterRegister();
        prod = login.loginApplication(this.email, this.password);
        Assert.assertEquals(login.getSuccessText(), "Login Successfully");
        Assert.assertEquals(prod.getTitleText(), "AUTOMATION");
    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.close();
    }


}
