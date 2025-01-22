package StepDefinitions.UITest.OrdersPage;

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
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ErrorHandlingStepDefImpl extends BaseTest {

    private LoginPage login;
    private ProductsPage prod;
    private CartPage cart;
    private OrdersPage orders;
    private CheckoutPage checkout;
    private OrderPage viewOrder;
    private OrderConfirmationPage confirmation;
    private int orderCount = 0;
    private String email = "santhoshsai4517@gmail.com";
    private String country = "India";
    private String orderId;
    private List<WebElement> products;
    private int noOfProductsToAddToCart = 2;


    @Given("User landed on ECommerece page orders page when api is down")
    public void userLandedOnECommerecePageOrdersPageWhenApiIsDown() throws IOException, InterruptedException {


        login = launchApplication();
        prod = login.loginApplication(email, "151Fa04124@4517");

        Thread.sleep(2000);

        products = prod.getProductList();

        //Adding random products to cart
        for (int i = 0; i < noOfProductsToAddToCart; ) {

            //Generating a random index to get product from products list
            int index = new Random().nextInt(products.size());
            String prodName = products.get(index).findElement(By.cssSelector(".card-body h5 b")).getText();
            //Adding products cart
            prod.addProductToCart(prodName);

            i = Integer.parseInt(prod.getCartCount());

            Thread.sleep(2000);
        }
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        Thread.sleep(4000);

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
                List.of(new RequestPattern(Optional.of("*get-orders-for-customer*"), Optional.empty(), Optional.empty())));

        devTools.send(Fetch.enable(patterns, Optional.empty()));

        devTools.addListener(Fetch.requestPaused(), request -> {
            devTools.send(Fetch.failRequest(request.getRequestId(), ErrorReason.FAILED));

        });


        orders = confirmation.clickOrdersLink();
        Assert.assertEquals(orders.getNoOrdersText(), "Loading....");
        Assert.assertEquals(orders.getUnknownError(), "Unknown error occured");
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/myorders");
    }


    @When("User clicks on delete button when api is down")
    public void userClicksOnDeleteButtonWhenApiIsDown() {


        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        Optional<List<RequestPattern>> patterns = Optional.of(
                List.of(new RequestPattern(Optional.of("*delete-order*"), Optional.empty(), Optional.empty())));

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
                List.of(new RequestPattern(Optional.of("*get-orders-details?id=*"), Optional.empty(), Optional.empty())));

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
    public void isDisplayedAndErrorOccurs(String displayMessage, String toastMessage) {
        Assert.assertEquals(viewOrder.getUnknownError(), toastMessage);
        Assert.assertEquals(viewOrder.getErrorText(), displayMessage);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/order-details/" + orderId);
    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.quit();
    }
}
