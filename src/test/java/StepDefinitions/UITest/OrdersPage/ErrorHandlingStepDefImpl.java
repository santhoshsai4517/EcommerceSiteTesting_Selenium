package StepDefinitions.UITest.OrdersPage;

import BaseTest.BaseTest;
import PageObjects.*;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
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
    private OrderPage viewOrder;
    private HashMap<String, Integer> prices = new HashMap<String, Integer>();
    private HashMap<String, String> productIds = new HashMap<String, String>();
    private OrderConfirmationPage confirmation;
    private int orderCount = 0;
    private String email = "santhoshsai4517@gmail.com";
    private String country = "India";
    private String orderId;


    @Given("User landed on ECommerece page orders page when api is down")
    public void userLandedOnECommerecePageOrdersPageWhenApiIsDown() throws IOException, InterruptedException {

        prices.put("zara", 31500);
        prices.put("adidas", 31500);
        prices.put("iphone", 231500);
        prices.put("qwerty", 11500);

        products.add("ZARA COAT 3");
        products.add("qwerty");
        products.add("ADIDAS ORIGINAL");
        products.add("IPHONE 13 PRO");

        productIds.put("zara", "6581ca399fd99c85e8ee7f45");
        productIds.put("adidas", "6581ca979fd99c85e8ee7faf");
        productIds.put("iphone", "6581cade9fd99c85e8ee7ff5");
        productIds.put("qwerty", "6701364cae2afd4c0b90113c");


        login = launchApplication();
        prod = login.loginApplication(email, "151Fa04124@4517");

        Thread.sleep(1000);

        for (String product : products)
            prod.addProductToCart(product.toUpperCase());

        cart = prod.gotoCart();
        checkout = cart.checkout();
        checkout.selectCountry("ind", country);

        confirmation = checkout.clickSubmitButton();

        Thread.sleep(1000);


    }

    @Then("Error occurs")
    public void errorOccurs() {

        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        Optional<List<RequestPattern>> patterns = Optional.of(
                Arrays.asList(new RequestPattern(Optional.of("*get-orders-for-customer*"), Optional.empty(), Optional.empty())));

        devTools.send(Fetch.enable(patterns, Optional.empty()));

        devTools.addListener(Fetch.requestPaused(), request -> {
            devTools.send(Fetch.failRequest(request.getRequestId(), ErrorReason.FAILED));

        });


        orders = confirmation.clickOrdersLink();
        Assert.assertEquals(orders.getNoOrdersText(), "Loading....");
        Assert.assertEquals(orders.getUnknownError(), "Unknown error occured");
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/myorders");
    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.close();
    }


    @When("User clicks on delete button when api is down")
    public void userClicksOnDeleteButtonWhenApiIsDown() {


        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        Optional<List<RequestPattern>> patterns = Optional.of(
                Arrays.asList(new RequestPattern(Optional.of("*delete-order*"), Optional.empty(), Optional.empty())));

        devTools.send(Fetch.enable(patterns, Optional.empty()));

        devTools.addListener(Fetch.requestPaused(), request -> {
            devTools.send(Fetch.failRequest(request.getRequestId(), ErrorReason.FAILED));

        });

        orders = confirmation.clickOrdersLink();
        orderCount = orders.getorders().size();
        orders.getorders().get(0).findElement(By.cssSelector(".btn-danger")).click();

    }

    @Then("{string} Error occurs")
    public void errorOccurs(String message) {
        Assert.assertEquals(orders.getUnknownError(), message);
        Assert.assertEquals(orderCount, orders.getorders().size());
    }

    @When("User clicks on view button when api is down")
    public void userClicksOnViewButtonWhenApiIsDown() {

        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        Optional<List<RequestPattern>> patterns = Optional.of(
                Arrays.asList(new RequestPattern(Optional.of("*get-orders-details?id=*"), Optional.empty(), Optional.empty())));

        devTools.send(Fetch.enable(patterns, Optional.empty()));

        devTools.addListener(Fetch.requestPaused(), request -> {
            devTools.send(Fetch.failRequest(request.getRequestId(), ErrorReason.FAILED));

        });

        orders = confirmation.clickOrdersLink();
        orderCount = orders.getorders().size();
        orderId = orders.getorders().get(0).findElement(By.cssSelector("tr.ng-star-inserted th")).getText();
        orders.getorders().get(0).findElement(By.cssSelector(".btn-primary")).click();
        viewOrder = new OrderPage(driver);

    }


    @Then("{string} is displayed and {string} Error occurs")
    public void isDisplayedAndErrorOccurs(String displayeMessage, String toastMessage) {
        Assert.assertEquals(viewOrder.getUnknownError(), toastMessage);
        Assert.assertEquals(viewOrder.getErrorText(), displayeMessage);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/order-details/" + orderId);
    }
}
