package StepDefinitions.UITest.CartPage;

import BaseTest.BaseTest;
import PageObjects.CartPage;
import PageObjects.LoginPage;
import PageObjects.OrdersPage;
import PageObjects.ProductsPage;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FunctionalityStepDefImpl extends BaseTest {

    private LoginPage login;
    private ProductsPage prod;
    private CartPage cart;
    private OrdersPage orders;
    private float totalPrice = 0;
    private HashMap<String, Integer> prices = new HashMap<String, Integer>();
    private int noOfProductsToAddToCart = 2;
    private List<WebElement> products;
    private Map<String,String> prodPrices = new HashMap<String, String>();
    private String cartTotal;

    @Given("User landed on ECommerece page cart page")
    public void user_landed_on_e_commerece_page_cart_page() throws IOException, InterruptedException {

        login = launchApplication();
        prod = login.loginApplication("santhoshsai4517@gmail.com", "151Fa04124@4517");
        Thread.sleep(2000);

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
                totalPrice += Float.parseFloat(prodPrice.split(" ")[1]);
            }

            cartCount = Integer.parseInt(prod.getCartCount());
        }

        if (totalPrice == Math.floor(totalPrice)) { // Check if the float is a whole number
            cartTotal = String.valueOf((int) totalPrice);
        }else cartTotal = String.format("%.2f", totalPrice);

        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        Thread.sleep(4000);

        cart = prod.gotoCart();
    }

    @When("User clicks on home button")
    public void user_clicks_on_home_button() {
        prod = prod.gotoProductsPage();

    }

    @Then("{string} message is displayed and Products page is displayed")
    public void messageIsDisplayedAndProductsPageIsDisplayed(String message) {
        Assert.assertEquals(prod.getTitleText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/dash");
    }

    @When("User clicks on orders button")
    public void userClicksOnOrdersButton() {
        orders = prod.gotoMyOrders();
    }

    @Then("Orders page is displayed")
    public void ordersPageIsDisplayed() {
        Assert.assertTrue(orders.goBackToShopButtonisVisible());
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/myorders");
    }


    @When("User clicks on signout button")
    public void userClicksOnSignoutButton() {
        login = prod.signout();
    }

    @Then("{string} is displayed and login page is displayed")
    public void isDisplayedAndLoginPageIsDisplayed(String message) {
        Assert.assertEquals(login.getLogoutText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/login");
    }

    @When("User clicks on cart button")
    public void userClicksOnCartButton() {
        cart = prod.gotoCart();
    }

    @Then("{string} message is displayed and cart page is displayed to user")
    public void messageIsDisplayedAndCartPageIsDisplayedToUser(String message) {
        Assert.assertEquals(cart.getCartText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/cart");
    }

    @When("User clicks on logo")
    public void userClicksOnLogo() {
        prod = prod.clickLogo();
    }

    @When("User clicks on continue shopping")
    public void userClicksOnContinueShopping() {
        prod = cart.continueShopping();
    }

    @Then("{string} toast is displayed and {string} message is displayed")
    public void toastIsDisplayedAndMessageIsDisplayed(String toastMsg, String textMsg) {

        Assert.assertEquals(cart.getNoProductsText(), textMsg);
        Assert.assertEquals(cart.getNoProductsToastText(), toastMsg);
    }

    @When("User adds products to cart and clicks on logout")
    public void userAddsProductsToCartAndClicksOnLogout() throws InterruptedException {

        Assert.assertEquals(cart.getCartCount(), noOfProductsToAddToCart);
        login = prod.signout();

        prod = login.loginApplication("santhoshsai4517@gmail.com", "151Fa04124@4517");
        Thread.sleep(2000);
        cart = prod.gotoCart();

    }

    @When("User adds products to cart")
    public void userAddsProductsToCart() {

        Assert.assertEquals(cart.getCartCount(), noOfProductsToAddToCart);

    }

    @Then("Validate product details and total price")
    public void validateProductDetailsAndTotalPrice() {

        List<WebElement> cartItems = cart.getCartProducts();

        for (WebElement cartItem : cartItems) {

            String productName = cartItem.findElement(By.cssSelector(".cartSection h3")).getText();

            Assert.assertTrue(prodPrices.containsKey(productName));
            Assert.assertEquals(cartItem.findElement(By.cssSelector(".cartSection p:nth-child(4)")).getText(), "MRP " + prodPrices.get(productName));
            Assert.assertEquals(cartItem.findElement(By.cssSelector(".cartSection p:nth-child(1)")).getText(), prodPrices.get(productName));
            Assert.assertEquals(cart.getSubTotal(), "$" + cartTotal);
            Assert.assertEquals(cart.getTotal(), "$" + cartTotal);
        }

    }

    @When("User adds products to cart and clicks on checkout")
    public void userAddsProductsToCartAndClicksOnCheckout() throws InterruptedException {
        cart.checkout();
    }

    @Then("Checkout page is displayed")
    public void checkoutPageIsDisplayed() {
        Assert.assertTrue(driver.getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/order?prop="));
    }

    @When("User adds products to cart and clicks on delete")
    public void userAddsProductsToCartAndClicksOnDelete() throws InterruptedException {

        List<WebElement> cartItems = cart.getCartProducts();

        cartItems.get(0).findElement(By.cssSelector(".btn-danger")).click();

        Thread.sleep(1000);

    }

    @Then("product is removed from cart")
    public void productIsRemovedFromCart() {
        Assert.assertEquals(cart.getCartCount(), noOfProductsToAddToCart - 1);

        List<WebElement> cartItems = cart.getCartProducts();


    }


    @When("User adds products to cart and clicks on buy now")
    public void userAddsProductsToCartAndClicksOnBuyNow() {

        List<WebElement> cartItems = cart.getCartProducts();
        int i = 0;
        for (WebElement cartItem : cartItems) {
            cartItems = cart.getCartProducts();
            cartItems.get(i).findElement(By.cssSelector(".btn-primary")).click();
            Assert.assertTrue(driver.getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/order?prop="));
            prod.gotoCart();
            i++;
        }

    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.quit();
    }


}
