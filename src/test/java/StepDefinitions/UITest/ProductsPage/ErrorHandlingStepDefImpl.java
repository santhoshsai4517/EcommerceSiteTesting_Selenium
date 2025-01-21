package StepDefinitions.UITest.ProductsPage;

import BaseTest.BaseTest;
import PageObjects.CartPage;
import PageObjects.LoginPage;
import PageObjects.OrdersPage;
import PageObjects.ProductsPage;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v130.fetch.Fetch;
import org.openqa.selenium.devtools.v130.fetch.model.RequestPattern;
import org.openqa.selenium.devtools.v130.network.model.ErrorReason;
import org.testng.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ErrorHandlingStepDefImpl extends BaseTest {

    private ProductsPage productspage;
    private CartPage cart;
    private OrdersPage orders;
    private LoginPage login;
    private boolean flag = true;
    private int i, k;

    @Given("I landed on ECommerece page products page after login")
    public void i_landed_on_e_commerece_page_products_page_after_login() throws FileNotFoundException, IOException {
        productspage = launchApplication().loginApplication("santhoshsai4517@gmail.com", "151Fa04124@4517");
    }

    @Then("No products are displayed")
    public void no_products_are_displayed() throws InterruptedException {

        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        Optional<List<RequestPattern>> patterns = Optional.of(Arrays
                .asList(new RequestPattern(Optional.of("*get-all-products*"), Optional.empty(), Optional.empty())));

        devTools.send(Fetch.enable(patterns, Optional.empty()));

        devTools.addListener(Fetch.requestPaused(), request -> {

            devTools.send(Fetch.failRequest(request.getRequestId(), ErrorReason.FAILED));

        });

        Assert.assertEquals(productspage.getProductList().size(), 0);

    }

    @When("User applies tshirt filter")
    public void user_applies_tshirt_filter() {

        productspage.applyTshirtFilter();
    }

    @Then("{string} error toast message is displayed")
    public void error_toast_message_is_displayed(String message) throws InterruptedException {

        Assert.assertEquals(productspage.getNoProductsError(), message);
        Assert.assertEquals(productspage.getProductList().size(), 0);
    }

    @When("User clicks on add to cart button")
    public void user_clicks_on_add_to_cart_button() throws InterruptedException {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        Optional<List<RequestPattern>> patterns = Optional.of(Arrays
                .asList(new RequestPattern(Optional.of("*add-to-cart*"), Optional.empty(), Optional.empty())));

        devTools.send(Fetch.enable(patterns, Optional.empty()));

        devTools.addListener(Fetch.requestPaused(), request -> {

            devTools.send(Fetch.failRequest(request.getRequestId(), ErrorReason.FAILED));

        });
        productspage.addProductToCart("QWERTY");
    }

    @Then("Product is not added to cart")
    public void product_is_not_added_to_cart() {
        Assert.assertEquals(productspage.getCartCount(), "");
    }

    @After
    public void afterScenario() {
//		Thread.sleep(2000);
        if (driver != null)
            driver.close();
    }
}
