package StepDefinitions.UITest.CheckoutPage;

import BaseTest.BaseTest;
import PageObjects.*;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v130.network.Network;
import org.openqa.selenium.devtools.v130.network.model.Request;
import org.openqa.selenium.devtools.v130.network.model.RequestId;
import org.openqa.selenium.devtools.v130.network.model.Response;
import org.testng.Assert;

import java.io.IOException;
import java.util.*;

public class FunctionalityStepDefImpl extends BaseTest {

    private LoginPage login;
    private ProductsPage prod;
    private CartPage cart;
    private OrdersPage orders;
    private CheckoutPage checkout;
    private boolean flag = true;
    private HashMap<String, String> prodPrices = new HashMap<String, String>();
    private int noOfProductsToAddToCart = 2;
    private List<WebElement> products;

    @Given("User landed on ECommerece page checkout page")
    public void userLandedOnECommerecePageCheckoutPage() throws IOException, InterruptedException {


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

            if(!prodPrices.containsKey(prodName.toLowerCase())) {
                prodPrices.put(prodName.toLowerCase(),prodPrice);

                prod.addProductToCart(prodName);
            }

            cartCount = Integer.parseInt(prod.getCartCount());
        }


        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        Thread.sleep(4000);

        cart = prod.gotoCart();

        Thread.sleep(2000);
        cart.getCartProducts().get(0).findElement(By.cssSelector(".btn-primary")).click();


    }

    @When("User clicks on home button in chekout page")
    public void userClicksOnHomeButtonInChekoutPage() {
        prod = prod.gotoProductsPage();
    }

    @Then("{string} message is displayed and Products page is displayed to user")
    public void messageIsDisplayedAndProductsPageIsDisplayedToUser(String message) {
        Assert.assertEquals(prod.getTitleText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/dash");
    }

    @When("User clicks on orders button in chekout page")
    public void userClicksOnOrdersButtonInChekoutPage() {
        orders = prod.gotoMyOrders();
    }

    @Then("Orders page is displayed to user")
    public void ordersPageIsDisplayedToUser() {
        Assert.assertTrue(orders.goBackToShopButtonisVisible());
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/myorders");
    }

    @When("User clicks on signout button in chekout page")
    public void userClicksOnSignoutButtonInChekoutPage() {
        login = prod.signout();
    }

    @Then("{string} is displayed and login page is displayed to user")
    public void isDisplayedAndLoginPageIsDisplayedToUser(String message) {
        Assert.assertEquals(login.getLogoutText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/login");
    }

    @When("User clicks on cart button in chekout page")
    public void userClicksOnCartButtonInChekoutPage() {
        cart = prod.gotoCart();
    }

    @Then("{string} message is displayed and cart page is displayed user")
    public void messageIsDisplayedAndCartPageIsDisplayedUser(String message) {
        Assert.assertEquals(cart.getCartText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/cart");
    }

    @When("User clicks on logo in chekout page")
    public void userClicksOnLogoInChekoutPage() {
        prod = prod.clickLogo();
    }


    @Then("Verify all product details")
    public void verifyAllProductDetails() throws InterruptedException {


        cart = prod.gotoCart();

        int i = 0;


        for (WebElement cartItem : cart.getCartProducts()) {
            // Get the current product details
            cartItem = cart.getCartProducts().get(i);
            String prodName = cartItem.findElement(By.cssSelector(".cartSection h3")).getText();
            String prodPrice = cartItem.findElement(By.cssSelector(".cartSection p:nth-child(1)")).getText();

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", cartItem);
            Thread.sleep(500);


            cartItem.findElement(By.cssSelector(".btn-primary")).click();


            // Verify details after redirection

            checkout = new CheckoutPage(driver);
            Assert.assertTrue(driver.getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/order?prop="));
            Assert.assertEquals(checkout.getProductCount(), 1);
            Assert.assertTrue(prodPrices.containsKey(checkout.getProductName().toLowerCase()));
            Assert.assertEquals(checkout.getProductPrice(), prodPrices.get(checkout.getProductName().toLowerCase()));

            // Navigate back to cart
            prod.gotoCart();

            i++; // Increment the index
        }

    }

    @When("User clicks on checkout button")
    public void userClicksOnCheckoutButton() throws InterruptedException {


        cart = prod.gotoCart();

        checkout = cart.checkout();

    }

    @Then("Verify all product details in checkout page")
    public void verifyAllProductDetailsInCheckoutPage() {
        Assert.assertEquals(checkout.getProductCount(), noOfProductsToAddToCart);
        List<WebElement> checkoutProducts = checkout.getProductDetails();

        for (WebElement checkoutProduct : checkoutProducts) {
            String prodName = checkoutProduct.findElement(By.cssSelector(".item__title")).getText();
            String prodPrice = checkoutProduct.findElement(By.cssSelector(".item__price")).getText();

//            System.out.println(prodName + " " + prodPrice);

            Assert.assertTrue(prodPrices.containsKey(prodName.toLowerCase()));
            Assert.assertEquals(prodPrice, prodPrices.get(prodName.toLowerCase()));
        }

    }

    @When("User fills country details and submits order")
    public void userFillsCountryDetailsAndSubmitsOrder() throws InterruptedException {
        prod = prod.gotoProductsPage();
        Thread.sleep(1000);

        cart = prod.gotoCart();

        checkout = cart.checkout();
        checkout.selectCountry("ind", "India");


        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), request ->

        {
            Request req = request.getRequest();
            if (req.getUrl().contains("create-order")) {
                RequestId requestId = request.getRequestId();
                String requestPayload = devTools.send(Network.getRequestPostData(requestId));
                System.out.println(requestPayload);
                JSONObject jsonResponse = new JSONObject(requestPayload);

                try {
                    JSONArray orders1 = jsonResponse.getJSONArray("orders");
                    Assert.assertEquals(orders1.length(), products.size(), "Product should match product count in cart");
                } catch (AssertionError e) {
                    e.printStackTrace();
                    flag = false; // Set flag if validation fails
                }

            }

        });

        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.responseReceived(), response -> {
            Response res = response.getResponse();

            if (res.getUrl().contains("create-order")) {

                RequestId requestId = response.getRequestId();
                String responsePayload = devTools.send(Network.getResponseBody(requestId)).getBody().toString();
                JSONObject json = new JSONObject(responsePayload);

                try {
                    Assert.assertEquals(json.getString("message"), "Order Placed Successfully", "Message mismatch");
                } catch (AssertionError e) {
                    e.printStackTrace();
                    flag = false; // Set flag if validation fails
                }

            }

        });

        checkout.clickSubmitButton();
    }

    @Then("Verify that order confirmation page is displayed")
    public void verifyThatOrderConfirmationPageIsDisplayed() throws InterruptedException {
//        System.out.println(driver.getCurrentUrl());
        Thread.sleep(1000);
        Assert.assertTrue(driver.getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/thanks?prop="));

    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.quit();
    }
}
