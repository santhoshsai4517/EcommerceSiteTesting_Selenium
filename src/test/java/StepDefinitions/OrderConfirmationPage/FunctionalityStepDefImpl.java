package StepDefinitions.OrderConfirmationPage;

import BaseTest.BaseTest;
import PageObjects.*;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v126.network.Network;
import org.openqa.selenium.devtools.v126.network.model.RequestId;
import org.openqa.selenium.devtools.v126.network.model.Response;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class FunctionalityStepDefImpl extends BaseTest {

    List<String> products = new ArrayList<String>();
    private LoginPage login;
    private ProductsPage prod;
    private CartPage cart;
    private OrdersPage orders;
    private CheckoutPage checkout;
    private HashMap<String, Integer> prices = new HashMap<String, Integer>();
    private HashMap<String, String> productIds = new HashMap<String, String>();
    private OrderConfirmationPage confirmation;
    private List<String> orderIds = new ArrayList<String>();

    @Given("User landed on ECommerece page order confirmation page")
    public void userLandedOnECommerecePageOrderConfirmationPage() throws IOException, InterruptedException {

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
        prod = login.loginApplication("santhoshsai4517@gmail.com", "151Fa04124@4517");

        Thread.sleep(1000);

        for (String product : products)
            prod.addProductToCart(product.toUpperCase());

        cart = prod.gotoCart();
        checkout = cart.checkout();
        checkout.selectCountry("ind", "India");
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
        Thread.sleep(1000);

    }

    @When("User clicks on home button in order confirmation page")
    public void userClicksOnHomeButtonInOrderConfirmationPage() {
        prod = prod.gotoProductsPage();
    }

    @Then("{string} message is displayed and user is redirected Products page")
    public void messageIsDisplayedAndUserIsRedirectedProductsPage(String message) {
        Assert.assertEquals(prod.getTitleText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/dash");
    }

    @When("User clicks on orders button in order confirmation page")
    public void userClicksOnOrdersButtonInOrderConfirmationPage() {
        orders = prod.gotoMyOrders();
    }

    @Then("user is redirected to Orders page is displayed")
    public void userIsRedirectedToOrdersPageIsDisplayed() throws InterruptedException {
        Assert.assertTrue(orders.goBackToShopButtonisVisible());
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/myorders");
    }

    @When("User clicks on signout button in order confirmation page")
    public void userClicksOnSignoutButtonInOrderConfirmationPage() {
        login = prod.signout();
    }

    @Then("{string} is displayed and user is redirected to login page")
    public void isDisplayedAndUserIsRedirectedToLoginPage(String message) {
        Assert.assertEquals(login.getLogoutText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/login");
    }

    @When("User clicks on cart button in order confirmation page")
    public void userClicksOnCartButtonInOrderConfirmationPage() {
        cart = prod.gotoCart();
    }

    @Then("{string} message is displayed and user is redirected to cart page")
    public void messageIsDisplayedAndUserIsRedirectedToCartPage(String message) {
        Assert.assertEquals(cart.getCartText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/cart");
    }

    @When("User clicks on logo in order confirmation page")
    public void userClicksOnLogoInOrderConfirmationPage() {
        prod = prod.clickLogo();
    }

    @When("User clicks on orders link in order confirmation page")
    public void userClicksOnOrdersLinkInOrderConfirmationPage() {
        orders = confirmation.clickOrdersLink();
    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.close();
    }

    @Then("Validate product details,order id,url")
    public void validateProductDetailsOrderIdUrl() {
        System.out.println(driver.getCurrentUrl() + " " + productIds.get("zara"));
        Assert.assertTrue(driver.getCurrentUrl().contains(productIds.get("zara")));
        Assert.assertTrue(driver.getCurrentUrl().contains(productIds.get("adidas")));
        Assert.assertTrue(driver.getCurrentUrl().contains(productIds.get("iphone")));
        Assert.assertTrue(driver.getCurrentUrl().contains(productIds.get("qwerty")));

        List<WebElement> orderedProds = confirmation.getOrderDetails();

        for (WebElement order : orderedProds) {
            String prodName = order.findElements(By.cssSelector(("td div.title"))).get(0).getText();
            String prodPrice = order.findElements(By.cssSelector(("td div.title"))).get(1).getText();
//            System.out.println(prodName + " " + prodPrice);
            Assert.assertTrue(products.contains(prodName));
            Assert.assertEquals(prices.get(prodName.split(" ")[0].toLowerCase()), Integer.parseInt(prodPrice.split(" ")[1]));
        }

        List<WebElement> Ids = confirmation.getOrderIds();

        for (WebElement orderId : Ids) {
            System.out.println(orderId.getText().split(" ")[1]);
            Assert.assertTrue(orderIds.contains(orderId.getText().split(" ")[1]));
        }

    }

    @When("User clicks on download buttons in order confirmation page")
    public void userClicksOnDownloadButtonsInOrderConfirmationPage() throws InterruptedException {
        List<WebElement> buttons = confirmation.getDownloadButtons();

        for (WebElement button : buttons) {
            button.click();
            Thread.sleep(1000);
            File file = null;
            if (button.getText().contains("CSV"))
                file = new File("C:\\Users\\santh\\Desktop\\Ecommerce Automation\\Selenium\\EcommercePractise\\Downloads" + "/order-invoice_santhoshsai4517.csv"); // Replace with your expected file name
            else if (button.getText().contains("Excel"))
                file = new File("C:\\Users\\santh\\Desktop\\Ecommerce Automation\\Selenium\\EcommercePractise\\Downloads" + "/order-invoice_santhoshsai4517.xlsx"); // Replace with your expected file name
            Assert.assertTrue(file.exists());
        }
    }


}
