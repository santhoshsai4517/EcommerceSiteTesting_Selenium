package StepDefinitions.UITest.CheckoutPage;

import BaseTest.BaseTest;
import PageObjects.*;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v130.fetch.Fetch;
import org.openqa.selenium.devtools.v130.fetch.model.RequestPattern;
import org.openqa.selenium.devtools.v130.network.model.ErrorReason;
import org.testng.Assert;

import java.io.IOException;
import java.util.*;

public class ErrorHandlingStepDefImpl extends BaseTest {

    private LoginPage login;
    private ProductsPage prod;
    private CartPage cart;
    private CheckoutPage checkout;
    private HashMap<String, String> prodPrices = new HashMap<String, String>();
    private int noOfProductsToAddToCart = 2;
    private List<WebElement> products;

    @Given("User landed on ECommerce page checkout page")
    public void userLandedOnECommercePageCheckoutPage() throws InterruptedException, IOException {


        login = launchApplication();
        prod = login.loginApplication("santhoshsai4517@gmail.com", "151Fa04124@4517");

        products = prod.getProductList();

        //Adding products to cart
        int cartCount;
        try {
            cartCount = Integer.parseInt(prod.getCartCount()); // Attempt to parse
        } catch (NumberFormatException e) {
            cartCount = 0; // Default to 0 if parsing fails
        }

        while (cartCount < noOfProductsToAddToCart) {
            //Generating a random index to get product from products list
            int index = new Random().nextInt(products.size());
            String prodName = products.get(index).findElement(By.cssSelector(".card-body h5 b")).getText();
            String prodPrice = products.get(index).findElement(By.cssSelector(".text-muted")).getText();

            if(!prodPrices.containsKey(prodName)) {
                prodPrices.put(prodName,prodPrice);

                prod.addProductToCart(prodName);
            }

            cartCount = Integer.parseInt(prod.getCartCount());
        }


        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        Thread.sleep(4000);

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

    @After
    public void afterScenario() {
        if (driver != null)
            driver.quit();
    }
}
