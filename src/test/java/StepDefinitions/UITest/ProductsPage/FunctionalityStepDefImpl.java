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
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v130.network.Network;
import org.openqa.selenium.devtools.v130.network.model.Request;
import org.openqa.selenium.devtools.v130.network.model.RequestId;
import org.openqa.selenium.devtools.v130.network.model.Response;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class FunctionalityStepDefImpl extends BaseTest {

    private final int noOfProductsToAddToCart = 3;
    private ProductsPage productspage;
    private CartPage cart;
    private OrdersPage orders;
    private LoginPage login;
    private boolean flag = true;
    private List<WebElement> products;


    @Given("I landed on ECommerece page products page")
    public void i_landed_on_e_commerece_page_products_page() throws IOException, InterruptedException {
        productspage = launchApplication().loginApplication("santhoshsai4517@gmail.com", "151Fa04124@4517");

        //Extracting products
        products = productspage.getProductList();
    }

    @Then("{string} logo message is displayed")
    public void logo_message_is_displayed(String message) throws InterruptedException {

        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), request ->

        {
            Request req = request.getRequest();
            if (req.getUrl().contains("get-all-products")) {
                RequestId requestId = request.getRequestId();
                String requestPayload = devTools.send(Network.getRequestPostData(requestId));


                JSONObject jsonResponse = new JSONObject(requestPayload);
                try {
                    Assert.assertEquals(jsonResponse.getString("productName"), "",
                            "productName should be an empty string");
                    Assert.assertTrue(jsonResponse.isNull("minPrice"), "minPrice should be null");
                    Assert.assertTrue(jsonResponse.isNull("maxPrice"), "maxPrice should be null");

                    // Validate arrays are empty
                    Assert.assertTrue(jsonResponse.getJSONArray("productCategory").isEmpty(),
                            "productCategory should be an empty array");
                    Assert.assertTrue(jsonResponse.getJSONArray("productSubCategory").isEmpty(),
                            "productSubCategory should be an empty array");
                    Assert.assertTrue(jsonResponse.getJSONArray("productFor").isEmpty(),
                            "productFor should be an empty array");

                } catch (AssertionError e) {
                    System.out.println(flag);
                    flag = false; // Set flag if validation fails
                }

            }

        });


        Thread.sleep(2000);
        Assert.assertEquals(productspage.getTitleText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/dash");
        Assert.assertTrue(flag);
    }

    @When("User clciks on cart button")
    public void user_clciks_on_cart_button() {
        cart = productspage.gotoCart();
    }

    @Then("{string} message is displayed and cart page is displayed")
    public void message_is_displayed_and_cart_page_is_displayed(String message) {
        Assert.assertEquals(cart.getCartText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/cart");
    }

    @When("User clciks on home button")
    public void user_clciks_on_home_button() {
        productspage.gotoProductsPage();
    }

    @Then("{string} message is displayed and products page is displayed")
    public void message_is_displayed_and_products_page_is_displayed(String message) {
        Assert.assertEquals(productspage.getTitleText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/dash");
    }

    @When("User clciks on orders button")
    public void user_clciks_on_orders_button() {
        orders = productspage.gotoMyOrders();
    }

    @Then("orders page is displayed")
    public void orders_page_is_displayed() {
        Assert.assertTrue(orders.goBackToShopButtonisVisible());
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/myorders");
    }

    @When("User clciks on signout button")
    public void user_clciks_on_signout_button() {
        login = productspage.signout();
    }

    @Then("{string} message is displayed and login page is displayed")
    public void message_is_displayed_and_login_page_is_displayed(String message) {
        Assert.assertEquals(login.getLogoutText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/login");
    }

    @When("User clciks on add cart button on a product")
    public void user_clciks_on_add_cart_button_on_a_product() throws InterruptedException {
        Thread.sleep(2000);
        DevTools devTools = driver.getDevTools();
        devTools.createSession();

        int i;
        for (i = 0; i < noOfProductsToAddToCart; ) {

            //Generating a random index to get product from products list
            int index = new Random().nextInt(products.size());
            String prodName = products.get(index).findElement(By.cssSelector(".card-body h5 b")).getText();
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
            devTools.addListener(Network.requestWillBeSent(), request ->

            {
                Request req = request.getRequest();
                if (req.getUrl().contains("add-to-cart")) {

                    RequestId requestId = request.getRequestId();
                    String requestPayload = devTools.send(Network.getRequestPostData(requestId));

                    JSONObject jsonResponse = new JSONObject(requestPayload);
                    System.out.println(jsonResponse);

                }

            });

            devTools.addListener(Network.responseReceived(), response -> {
                Response res = response.getResponse();

                if (res.getUrl().contains("add-to-cart")) {

                    RequestId requestId = response.getRequestId();
                    String responsePayload = devTools.send(Network.getResponseBody(requestId)).getBody();
                    JSONObject json = new JSONObject(responsePayload);

                    try {
                        Assert.assertEquals(json.getString("message"), "Product Added To Cart", "Message error");
                    } catch (AssertionError e) {

                        flag = false; // Set flag if validation fails

                    }

                }

            });


            String message = productspage.addProductToCart(prodName);

            i = Integer.parseInt(productspage.getCartCount());

            Assert.assertEquals(message, "Product Added To Cart");

            Thread.sleep(2000);
        }

        Assert.assertTrue(flag);
    }

    @Then("{string} message is displayed and product is added to cart")
    public void message_is_displayed_and_product_is_added_to_cart(String message) {
        Assert.assertEquals(productspage.getCartCount(), Integer.toString(noOfProductsToAddToCart));
    }

    @When("User searches for a product")
    public void user_searches_for_a_product() throws InterruptedException {
        Thread.sleep(2000);
        String prodname = products.get(0).findElement(By.cssSelector(".card-body h5 b")).getText();
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), request ->

        {
            Request req = request.getRequest();
            if (req.getUrl().contains("get-all-products")) {
                RequestId requestId = request.getRequestId();
                String requestPayload = devTools.send(Network.getRequestPostData(requestId));

                JSONObject jsonResponse = new JSONObject(requestPayload);
                System.out.println(jsonResponse.getString("productName"));
                try {
                    Assert.assertEquals(jsonResponse.getString("productName"), prodname,
                            "productName should be an empty string");
                    Assert.assertTrue(jsonResponse.isNull("minPrice"), "minPrice should be null");
                    Assert.assertTrue(jsonResponse.isNull("maxPrice"), "maxPrice should be null");

                    // Validate arrays are empty
                    Assert.assertTrue(jsonResponse.getJSONArray("productCategory").isEmpty(),
                            "productCategory should be an empty array");
                    Assert.assertTrue(jsonResponse.getJSONArray("productSubCategory").isEmpty(),
                            "productSubCategory should be an empty array");
                    Assert.assertTrue(jsonResponse.getJSONArray("productFor").isEmpty(),
                            "productFor should be an empty array");

                } catch (AssertionError e) {

                    e.printStackTrace();
                    flag = false; // Set flag if validation fails
                }

            }

        });

        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.responseReceived(), response -> {
            Response res = response.getResponse();

            if (res.getUrl().contains("get-all-products")) {

                RequestId requestId = response.getRequestId();
                String responsePayload = devTools.send(Network.getResponseBody(requestId)).getBody();
                JSONObject json = new JSONObject(responsePayload);

                System.out.println(json);


            }

        });

        productspage.searchProduct(prodname);
        Assert.assertTrue(flag);
    }

    @When("User filters using min max price")
    public void user_filters_using_min_max_price() throws InterruptedException {
        Thread.sleep(2000);
        productspage.minPrice("300");
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), request ->

        {
            Request req = request.getRequest();
            if (req.getUrl().contains("get-all-products")) {
                RequestId requestId = request.getRequestId();
                String requestPayload = devTools.send(Network.getRequestPostData(requestId));
                System.out.println(requestPayload);
                JSONObject jsonResponse = new JSONObject(requestPayload);
                System.out.println(jsonResponse.getString("productName"));
                try {
                    Assert.assertEquals(jsonResponse.getString("productName"), "",
                            "productName should be an empty string");
                    Assert.assertEquals(jsonResponse.get("minPrice").toString(), "300", "minPrice should be null");
                    Assert.assertEquals(jsonResponse.get("maxPrice").toString(), "50000", "maxPrice should be null");

                    // Validate arrays are empty
                    Assert.assertTrue(jsonResponse.getJSONArray("productCategory").isEmpty(),
                            "productCategory should be an empty array");
                    Assert.assertTrue(jsonResponse.getJSONArray("productSubCategory").isEmpty(),
                            "productSubCategory should be an empty array");
                    Assert.assertTrue(jsonResponse.getJSONArray("productFor").isEmpty(),
                            "productFor should be an empty array");

                } catch (AssertionError e) {
                    e.printStackTrace();
                    flag = false; // Set flag if validation fails
                }

            }

        });

        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.responseReceived(), response -> {
            Response res = response.getResponse();

            if (res.getUrl().contains("get-all-products")) {

                RequestId requestId = response.getRequestId();
                String responsePayload = devTools.send(Network.getResponseBody(requestId)).getBody();
                JSONObject json = new JSONObject(responsePayload);

                System.out.println(json);

            }

        });

        productspage.maxPrice("50000");
        Assert.assertTrue(flag);
    }

    @When("User filters using categories")
    public void user_filters_using_categories() throws InterruptedException {
        Thread.sleep(2000);

        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), request ->

        {
            Request req = request.getRequest();
            if (req.getUrl().contains("get-all-products")) {
                RequestId requestId = request.getRequestId();
                String requestPayload = devTools.send(Network.getRequestPostData(requestId));
                System.out.println(requestPayload);
                JSONObject jsonResponse = new JSONObject(requestPayload);

                try {
                    Assert.assertEquals(jsonResponse.getString("productName"), "",
                            "productName should be an empty string");
                    Assert.assertTrue(jsonResponse.isNull("minPrice"), "minPrice should be null");
                    Assert.assertTrue(jsonResponse.isNull("maxPrice"), "maxPrice should be null");

                    JSONArray productCategoryArray = jsonResponse.getJSONArray("productCategory");
                    Assert.assertEquals(productCategoryArray.length(), 1,
                            "productCategory should contain exactly one item");
                    Assert.assertEquals(productCategoryArray.getString(0), "fashion",
                            "productCategory should contain 'fashion'");

                    Assert.assertTrue(jsonResponse.getJSONArray("productSubCategory").isEmpty(),
                            "productSubCategory should be an empty array");
                    Assert.assertTrue(jsonResponse.getJSONArray("productFor").isEmpty(),
                            "productFor should be an empty array");

                } catch (AssertionError e) {

                    e.printStackTrace();
                    flag = false; // Set flag if validation fails
                }

            }

        });

        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.responseReceived(), response -> {
            Response res = response.getResponse();

            if (res.getUrl().contains("get-all-products")) {

                RequestId requestId = response.getRequestId();
                String responsePayload = devTools.send(Network.getResponseBody(requestId)).getBody();
                JSONObject json = new JSONObject(responsePayload);

                System.out.println(json);

            }

        });

        productspage.applyFashionFilter();

        Assert.assertTrue(flag);
    }

    @After
    public void afterScenario() throws InterruptedException {
        Thread.sleep(2000);
        if (driver != null)
            driver.quit();
    }


}
