package StepDefinitions.UITest.ProductPage;

import BaseTest.BaseTest;
import PageObjects.*;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class FunctionalityStepDefImpl extends BaseTest {

    private LoginPage login;
    private ProductsPage prod;
    private ProductPage product;
    private CartPage cart;
    private OrdersPage orders;
    private List<WebElement> products;
    private String productName, productPrice;

    @Given("User lands on ECommerece page and product page")
    public void userLandsOnECommerecePageAndProductPage() throws IOException, InterruptedException {


        login = launchApplication();
        prod = login.loginApplication("santhoshsai4517@gmail.com", "151Fa04124@4517");
        products = prod.getProductList();

        Thread.sleep(2000);

        int index = new Random().nextInt(products.size());
        productName = products.get(index).findElement(By.cssSelector(".card-body h5 b")).getText();
        productPrice = products.get(index).findElement(By.cssSelector(".text-muted")).getText();


        product = prod.viewProductDetails(productName);


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

    @When("User clciked on continue shopping button")
    public void userClcikedOnContinueShoppingButton() throws InterruptedException {
        prod = prod.gotoProductsPage();


        product = prod.viewProductDetails(productName);
        Thread.sleep(1000);
        Assert.assertEquals(product.getProductName(), productName);
        Assert.assertEquals(product.getProductPrice(), productPrice);
        Assert.assertTrue(driver.getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/product-details/"));
        prod = product.continueShopping();


    }

    @When("User clciked on add to cart button")
    public void userClcikedOnAddToCartButton() throws InterruptedException {
        prod = prod.gotoProductsPage();


        product = prod.viewProductDetails(productName);
        Thread.sleep(1000);
        product.addToCart();
        Assert.assertEquals(product.getProductAddedToCartToastText(), "Product Added To Cart");

        Thread.sleep(1000);
        prod = product.continueShopping();


        cart = prod.gotoCart();
    }

    @Then("products are added to cart")
    public void productsAreAddedToCart() {
        Assert.assertEquals(cart.getCartCount(), 1);
    }


    @After
    public void afterScenario() {
        if (driver != null)
            driver.quit();
    }

}
