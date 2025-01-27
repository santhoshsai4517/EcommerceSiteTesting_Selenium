package StepDefinitions.UITest.OrderPage;

import BaseTest.BaseTest;
import PageObjects.*;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.io.IOException;

public class FunctionalityStepDefImpl extends BaseTest {


    private LoginPage login;
    private ProductsPage prod;
    private CartPage cart;
    private OrdersPage orders;
    private OrderPage viewOrder;
    private String email = "santhoshsai4517@gmail.com";

    @Given("User landed on ECommerece page order page")
    public void userLandedOnECommerecePageOrderPage() throws IOException {

        login = launchApplication();
        prod = login.loginApplication(email, "151Fa04124@4517");
        orders = prod.gotoMyOrders();
        orders.getorders().get(0).findElement(By.cssSelector(".btn-primary")).click();

    }

    @When("User clicks on home button in order page")
    public void userClicksOnHomeButtonInOrderPage() throws InterruptedException {
        prod = prod.gotoProductsPage();
        Thread.sleep(1000);
    }

    @Then("{string} message is displayed and user is redirected Products page from order page")
    public void messageIsDisplayedAndUserIsRedirectedProductsPageFromOrderPage(String message) {
        Assert.assertEquals(prod.getTitleText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/dash");
    }

    @When("User clicks on orders button in order page")
    public void userClicksOnOrdersButtonInOrderPage() {
        orders = prod.gotoMyOrders();
    }

    @Then("user is redirected to Orders page is displayed from order page")
    public void userIsRedirectedToOrdersPageIsDisplayedFromOrderPage() {
        Assert.assertTrue(orders.goBackToShopButtonisVisible());
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/myorders");
    }


    @When("User clicks on signout button in order page")
    public void userClicksOnSignoutButtonInOrderPage() {
        login = prod.signout();
    }

    @Then("{string} is displayed and user is redirected to login page from order page")
    public void isDisplayedAndUserIsRedirectedToLoginPageFromOrderPage(String message) {
        Assert.assertEquals(login.getLogoutText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/login");
    }

    @When("User clicks on cart button in order page")
    public void userClicksOnCartButtonInOrderPage() {
        cart = prod.gotoCart();
    }

    @Then("{string} message is displayed and user is redirected to cart page from order page")
    public void messageIsDisplayedAndUserIsRedirectedToCartPageFromOrderPage(String message) {
        Assert.assertEquals(cart.getCartText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/cart");
    }

    @When("User clicks on logo in order page")
    public void userClicksOnLogoInOrderPage() {
        prod = prod.clickLogo();
    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.quit();
    }
}
