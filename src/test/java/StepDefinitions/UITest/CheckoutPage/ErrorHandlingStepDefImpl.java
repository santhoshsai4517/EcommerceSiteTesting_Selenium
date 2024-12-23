package StepDefinitions.UITest.CheckoutPage;

import BaseTest.BaseTest;
import PageObjects.*;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v127.fetch.Fetch;
import org.openqa.selenium.devtools.v127.fetch.model.RequestPattern;
import org.openqa.selenium.devtools.v127.network.model.ErrorReason;
import org.testng.Assert;

import java.io.IOException;
import java.util.*;

public class ErrorHandlingStepDefImpl extends BaseTest {
    List<String> products = new ArrayList<String>();
    private LoginPage login;
    private ProductsPage prod;
    private CartPage cart;
    private OrdersPage orders;
    private CheckoutPage checkout;
    private boolean flag = true;
    private HashMap<String, Integer> prices = new HashMap<String, Integer>();

    @Given("User landed on ECommerce page checkout page")
    public void userLandedOnECommercePageCheckoutPage() throws InterruptedException, IOException {

        prices.put("zara", 31500);
        prices.put("adidas", 31500);
        prices.put("iphone", 231500);
        prices.put("qwerty", 11500);

        products.add("ZARA COAT 3");
        products.add("qwerty");
        products.add("ADIDAS ORIGINAL");
        products.add("IPHONE 13 PRO");


        login = launchApplication();
        prod = login.loginApplication("santhoshsai4517@gmail.com", "151Fa04124@4517");

        prod.addProductToCart(products.get(0).toUpperCase());

        cart = prod.gotoCart();

    }

    @When("User clicks on submit button without country details")
    public void userClicksOnSubmitButtonWithoutCountryDetails() throws InterruptedException {
        checkout = cart.checkout();
        checkout.clickSubmitButton();

    }

    @Then("{string} message is displayed and order is not submitted")
    public void messageIsDisplayedAndOrderIsNotSubmitted(String message) {
        Assert.assertEquals(checkout.getErrorText(), message);
        Assert.assertTrue(driver.getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/order?prop"));
    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.close();
    }

    @When("User clicks on submit button and api is failed")
    public void userClicksOnSubmitButtonAndApiIsFailed() throws InterruptedException {
        checkout = cart.checkout();
        checkout.selectCountry("ind", "India");

        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        Optional<List<RequestPattern>> patterns = Optional.of(
                Arrays.asList(new RequestPattern(Optional.of("*create-order*"), Optional.empty(), Optional.empty())));

        devTools.send(Fetch.enable(patterns, Optional.empty()));

        devTools.addListener(Fetch.requestPaused(), request -> {
            devTools.send(Fetch.failRequest(request.getRequestId(), ErrorReason.FAILED));

        });

        checkout.clickSubmitButton();
    }

    @Then("{string} unknwon message is displayed and order is not submitted")
    public void unknwonMessageIsDisplayedAndOrderIsNotSubmitted(String message) {
        Assert.assertEquals(checkout.getUnknownError(), message);
        Assert.assertTrue(driver.getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/order?prop"));
    }
}
