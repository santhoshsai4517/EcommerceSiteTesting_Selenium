package StepDefinitions.UITest.ProductPage;

import BaseTest.BaseTest;
import PageObjects.*;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FunctionalityStepDefImpl extends BaseTest {

    List<String> products = new ArrayList<String>();
    private LoginPage login;
    private ProductsPage prod;
    private ProductPage product;
    private CartPage cart;
    private OrdersPage orders;
    private HashMap<String, Integer> prices = new HashMap<String, Integer>();

    @Given("User lands on ECommerece page and product page")
    public void userLandsOnECommerecePageAndProductPage() throws IOException, InterruptedException {

        prices.put("adidas", 31500);
        prices.put("iphone", 231500);
        prices.put("qwerty", 11500);
        
        products.add("QWERTY");
        products.add("ADIDAS ORIGINAL");
        products.add("IPHONE 13 PRO");

        login = launchApplication();
        prod = login.loginApplication("santhoshsai4517@gmail.com", "151Fa04124@4517");
//        Thread.sleep(1000);
        product = prod.viewProductDetails("QWERTY");

    }

    @When("User clicks on logo button")
    public void userClicksOnLogoButton() {
        prod = prod.clickLogo();
    }


    @Then("{string} logo message is displayed and products page is displayed")
    public void logoMessageIsDisplayedAndProductsPageIsDisplayed(String message) {
        Assert.assertEquals(prod.getTitleText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/dash");
    }

    @When("User clciked on cart button")
    public void userClcikedOnCartButton() {
        cart = prod.gotoCart();
    }

    @Then("{string} message is displayed and cart page")
    public void messageIsDisplayedAndCartPage(String message) {
        Assert.assertEquals(cart.getCartText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/cart");
    }

    @When("User clciked on home button")
    public void userClcikedOnHomeButton() {
        prod = prod.gotoProductsPage();
    }

    @When("User clciked on orders button")
    public void userClcikedOnOrdersButton() {
        orders = prod.gotoMyOrders();
    }

    @Then("orders page is displayed to user")
    public void ordersPageIsDisplayedToUser() {
        Assert.assertTrue(orders.goBackToShopButtonisVisible());
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/myorders");
    }

    @When("User clciked on signout button")
    public void userClcikedOnSignoutButton() {
        login = prod.signout();
    }

    @Then("{string} message is displayed and login page")
    public void messageIsDisplayedAndLoginPage(String message) {
        Assert.assertEquals(login.getLogoutText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/login");
    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.close();
    }

    @When("User clciked on continue shopping button")
    public void userClcikedOnContinueShoppingButton() throws InterruptedException {
        prod = prod.gotoProductsPage();

        for (String productName : products) {
            product = prod.viewProductDetails(productName);
            Thread.sleep(1000);
            Assert.assertEquals(product.getProductName(), productName);
            Assert.assertEquals(product.getProductPrice(), "$ " + prices.get(productName.split(" ")[0].toLowerCase()));
            Assert.assertTrue(driver.getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/product-details/"));
            prod = product.continueShopping();
        }

    }

    @When("User clciked on add to cart button")
    public void userClcikedOnAddToCartButton() throws InterruptedException {
        prod = prod.gotoProductsPage();
        int i = 0;
        for (String productName : products) {

            product = prod.viewProductDetails(productName);
            Thread.sleep(1000);
            product.addToCart();
            Assert.assertEquals(product.getProductAddedToCartToastText(), "Product Added To Cart");
            i++;
            Thread.sleep(1000);
            Assert.assertEquals(prod.getCartCount(), String.valueOf(i));
            prod = product.continueShopping();

        }
        cart = prod.gotoCart();
    }

    @Then("products are added to cart")
    public void productsAreAddedToCart() {
        Assert.assertEquals(cart.getCartCount(), products.size());
    }
}
