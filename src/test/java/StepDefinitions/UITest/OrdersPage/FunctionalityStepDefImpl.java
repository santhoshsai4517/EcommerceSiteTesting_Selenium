package StepDefinitions.UITest.OrdersPage;

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
    private OrderPage viewOrder;
    private HashMap<String, Integer> prices = new HashMap<String, Integer>();
    private HashMap<String, String> productIds = new HashMap<String, String>();
    private OrderConfirmationPage confirmation;
    private List<String> orderIds = new ArrayList<String>();
    private boolean flag = true;
    private String email = "santhoshsai4517@gmail.com";
    private String country = "India";
    private List<WebElement> products;
    private int noOfProductsToAddToCart = 2;
    private Map<String,String> prodPrices = new HashMap<String, String>();

    @Given("User landed on ECommerece page orders page")
    public void userLandedOnECommerecePageOrdersPage() throws IOException, InterruptedException {

        login = launchApplication();
        prod = login.loginApplication(email, "151Fa04124@4517");

        Thread.sleep(1000);

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
        checkout = cart.checkout();
        checkout.selectCountry("ind", country);

        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));


        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.responseReceived(), response -> {
            Response res = response.getResponse();

            if (res.getUrl().contains("create-order")) {

                RequestId requestId = response.getRequestId();
                String responsePayload = devTools.send(Network.getResponseBody(requestId)).getBody().toString();
                JSONObject json = new JSONObject(responsePayload);

                JSONArray orderIDs = json.getJSONArray("orders");
                for (int i = 0; i < orderIDs.length(); i++) {
                    orderIds.add(orderIDs.get(i).toString());
                }
            }

        });

        confirmation = checkout.clickSubmitButton();
        orders = confirmation.clickOrdersLink();

    }

    @When("User clicks on home button in orders page")
    public void userClicksOnHomeButtonInOrdersPage() {
        prod = prod.gotoProductsPage();
    }

    @Then("{string} message is displayed and user is redirected Products page from orders page")
    public void messageIsDisplayedAndUserIsRedirectedProductsPageFromOrdersPage(String message) {
        Assert.assertEquals(prod.getTitleText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/dash");
    }

    @When("User clicks on orders button in orders page")
    public void userClicksOnOrdersButtonInOrdersPage() {
        orders = prod.gotoMyOrders();
    }

    @Then("user is redirected to Orders page is displayed from orders page")
    public void userIsRedirectedToOrdersPageIsDisplayedFromOrdersPage() {
        Assert.assertTrue(orders.goBackToShopButtonisVisible());
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/myorders");
    }

    @When("User clicks on signout button in orders page")
    public void userClicksOnSignoutButtonInOrdersPage() {
        login = prod.signout();
    }

    @Then("{string} is displayed and user is redirected to login page from orders page")
    public void isDisplayedAndUserIsRedirectedToLoginPageFromOrdersPage(String message) {
        Assert.assertEquals(login.getLogoutText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/login");
    }

    @When("User clicks on cart button in orders page")
    public void userClicksOnCartButtonInOrdersPage() {
        cart = prod.gotoCart();
    }

    @Then("{string} message is displayed and user is redirected to cart page from orders page")
    public void messageIsDisplayedAndUserIsRedirectedToCartPageFromOrdersPage(String message) {
        Assert.assertEquals(cart.getCartText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/cart");
    }

    @When("User clicks on logo in orders page")
    public void userClicksOnLogoInOrdersPage() {
        prod = prod.clickLogo();
    }

    @When("User clicks on go back to shopping button in orders page")
    public void userClicksOnGoBackToShoppingButtonInOrdersPage() throws InterruptedException {
        prod = orders.gotoProductPage();
    }

    @When("User clicks on go back to cart button in orders page")
    public void userClicksOnGoBackToCartButtonInOrdersPage() throws InterruptedException {
        cart = orders.gotoCartPage();
    }


    @Given("User landed on ECommerece page orders page with no orders")
    public void userLandedOnECommerecePageOrdersPageWithNoOrders() throws IOException, InterruptedException {
        login = launchApplication();
        prod = login.loginApplication("s12345@gmail.com", "123");

        Thread.sleep(1000);

        orders = prod.gotoMyOrders();
        Thread.sleep(1000);
    }

    @Then("If no orders then {string} message is displayed")
    public void ifNoOrdersThenMessageIsDisplayed(String message) {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.responseReceived(), response -> {
            Response res = response.getResponse();

            if (res.getUrl().contains("get-orders-for-customer")) {

                RequestId requestId = response.getRequestId();
                String responsePayload = devTools.send(Network.getResponseBody(requestId)).getBody().toString();
                JSONObject json = new JSONObject(responsePayload);

                try {
                    Assert.assertEquals(json.getString("message"), "No Orders", "Message mismatch");


                } catch (AssertionError e) {
                    e.printStackTrace();
                    flag = false; // Set flag if validation fails
                }

            }

        });

        Assert.assertTrue(flag);
        Assert.assertTrue(orders.getNoOrdersText().contains(message));
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/myorders");
    }

    @Then("Orders are displayed")
    public void ordersAreDisplayed() {

        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.responseReceived(), response -> {
            Response res = response.getResponse();

            if (res.getUrl().contains("get-orders-for-customer")) {

                RequestId requestId = response.getRequestId();
                String responsePayload = devTools.send(Network.getResponseBody(requestId)).getBody().toString();
                JSONObject json = new JSONObject(responsePayload);

                try {
                    Assert.assertEquals(json.getString("message"), "Order Placed Successfully", "Message mismatch");
                    Assert.assertEquals(orders.getorders().size(), json.getInt("count"));

                } catch (AssertionError e) {
                    e.printStackTrace();
                    flag = false; // Set flag if validation fails
                }

            }

        });

        Assert.assertTrue(flag);

        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/myorders");
    }

    @When("User clicks on delete button")
    public void userClicksOnDeleteButton() throws InterruptedException {
        int orderCount = orders.getorders().size(); // Initial count
        for (int i = 0; i < orderCount; i++) {
            // Re-fetch the updated list in each iteration
            List<WebElement> orderDetails = orders.getorders();

            // Get the current order element
            WebElement order = orderDetails.get(i);
            WebElement prodName = order.findElement(By.cssSelector("tr.ng-star-inserted td:nth-child(3)"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", prodName);

            if (prodName.getText().equals("ADIDAS ORIGINAL")) {

                DevTools devTools = driver.getDevTools();
                devTools.createSession();
                devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
                devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
                devTools.addListener(Network.responseReceived(), response -> {
                    Response res = response.getResponse();

                    if (res.getUrl().contains("delete-order")) {

                        RequestId requestId = response.getRequestId();
                        String responsePayload = devTools.send(Network.getResponseBody(requestId)).getBody().toString();
                        JSONObject json = new JSONObject(responsePayload);

                        try {
                            Assert.assertEquals(json.getString("message"), "Orders Deleted Successfully", "Message mismatch");

                        } catch (AssertionError e) {
                            e.printStackTrace();
                            flag = false; // Set flag if validation fails
                        }

                    }

                });

                Assert.assertTrue(flag);


                order.findElement(By.cssSelector(".btn-danger")).click();
                Assert.assertEquals(orders.getToastText(), "Orders Deleted Successfully");
                Thread.sleep(2000);

                // Break out of the loop if only one deletion is intended
                Assert.assertEquals(orders.getorders().size(), --orderCount);


                i--; // Adjust index since the list has shifted

                Thread.sleep(2000);
            }
        }
    }

    @When("User clicks on view button")
    public void userClicksOnViewButton() throws InterruptedException {
        int orderCount = orders.getorders().size(); // Initial count
        for (int i = 0; i < orderCount; i++) {
            // Re-fetch the updated list in each iteration
            List<WebElement> orderDetails = orders.getorders();
            System.out.println(orderIds);

            // Get the current order element
            WebElement order = orderDetails.get(i);
            WebElement orderIdElm = order.findElement(By.cssSelector("tr.ng-star-inserted th"));
            String orderId = orderIdElm.getText();
            String prodName = order.findElement(By.cssSelector("tr.ng-star-inserted td:nth-child(3)")).getText();
            String prodPrice = prodPrices.get(prodName.toLowerCase());
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", orderIdElm);

            if (orderIds.contains(orderId)) {
                System.out.println(orderId);
                DevTools devTools = driver.getDevTools();
                devTools.createSession();
                devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
                devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
                devTools.addListener(Network.responseReceived(), response -> {
                    Response res = response.getResponse();

                    if (res.getUrl().contains("get-orders-details?id=" + orderId)) {
                        RequestId requestId = response.getRequestId();
                        String responsePayload = devTools.send(Network.getResponseBody(requestId)).getBody().toString();
                        JSONObject json = new JSONObject(responsePayload);


                        try {
                            JSONObject prodData = json.getJSONObject("data");

                            Assert.assertEquals(json.getString("message"), "Orders fetched for customer Successfully", "Message mismatch");
                            Assert.assertEquals(prodData.getString("productName"), prodName, "Product name mismatch");
                            Assert.assertEquals(prodData.getString("orderPrice"), prodPrice.split(" ")[1], "Product price mismatch");
                            Assert.assertEquals(prodData.getString("country"), country, "Country mismatch");
                            Assert.assertEquals(prodData.getString("orderBy"), email, "Order status mismatch");
                            Assert.assertEquals(prodData.getString("_id"), orderId, "Order ID mismatch");
//                            Assert.assertEquals(prodData.getString("productOrderedId"), productIds.get(prodName.split(" ")[0].toLowerCase()), "Product ID mismatch");


                        } catch (AssertionError e) {
                            e.printStackTrace();
                            flag = false; // Set flag if validation fails
                        }

                    }

                });

                Assert.assertTrue(flag);


                order.findElement(By.cssSelector(".btn-primary")).click();
                viewOrder = new OrderPage(driver);

                Thread.sleep(2000);

                Assert.assertEquals(viewOrder.orderID(), orderId);
                Assert.assertEquals(viewOrder.productTitle(), prodName);
                Assert.assertEquals(viewOrder.productPrice(), prodPrice);

                String billingEmail = viewOrder.getAddressElms().get(0).getText();
                String billingCountry = viewOrder.getAddressElms().get(1).getText();
                String deliveryEmail = viewOrder.getAddressElms().get(2).getText();
                String deliveryCountry = viewOrder.getAddressElms().get(3).getText();

                Assert.assertEquals(billingEmail, email);
                Assert.assertTrue(billingCountry.contains(country));

                Assert.assertEquals(deliveryEmail, email);
                Assert.assertTrue(deliveryCountry.contains(country));


                Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/order-details/" + orderId);
                orders = viewOrder.gotorders();

                Thread.sleep(2000);
            }
        }
    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.quit();
    }

}
