package StepDefinitions.CartPage;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FunctionalityStepDefImpl extends BaseTest {

    private LoginPage login;
    private ProductsPage prod;
    private CartPage cart;
    private OrdersPage orders;
    private int totalPrice = 0;
    private HashMap<String,Integer> prices = new HashMap<String,Integer>();
    List<String> products = new ArrayList<String>();


    @Given("User landed on ECommerece page cart page")
    public void user_landed_on_e_commerece_page_cart_page() throws IOException, InterruptedException {
        prices.put("zara",31500);
        prices.put("adidas",31500);
        prices.put("iphone",231500);
        prices.put("qwerty",11500);

        login = launchApplication();
        prod = login.loginApplication("santhoshsai4517@gmail.com","151Fa04124@4517");
        Thread.sleep(2000);
        cart = prod.gotoCart();
    }
    @When("User clicks on home button")
    public void user_clicks_on_home_button() {
        prod = prod.gotoProductsPage();

    }

    @Then("{string} message is displayed and Products page is displayed")
    public void messageIsDisplayedAndProductsPageIsDisplayed(String message) {
        Assert.assertEquals(prod.getTitleText(),message);
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
    public void toastIsDisplayedAndMessageIsDisplayed(String toastMsg, String textMsg){

        Assert.assertEquals(cart.getNoProductsText(), textMsg);
        Assert.assertEquals(cart.getNoProductsToastText(), toastMsg);
    }

    @When("User adds products to cart and clicks on logout")
    public void userAddsProductsToCartAndClicksOnLogout() throws InterruptedException {
        prod = prod.gotoProductsPage();
        List<String> products = new ArrayList<String>();
        products.add("ZARA COAT 3");
        products.add("qwerty");
        for (String product : products) {
            prod.addProductToCart(product.toUpperCase());
        }
        cart = prod.gotoCart();
        Assert.assertEquals(cart.getCartCount(),products.size());
        login = prod.signout();

        prod = login.loginApplication("santhoshsai4517@gmail.com","151Fa04124@4517");
        Thread.sleep(2000);
        cart = prod.gotoCart();

    }

    @When("User adds products to cart")
    public void userAddsProductsToCart() {

        prod = prod.gotoProductsPage();

        products.add("ZARA COAT 3");
        products.add("QWERTY");
        for (String product : products) {
            prod.addProductToCart(product);
            totalPrice += prices.get(product.split(" ")[0].toLowerCase());
        }
        cart = prod.gotoCart();
        Assert.assertEquals(cart.getCartCount(),products.size());

    }

    @Then("Validate product details and total price")
    public void validateProductDetailsAndTotalPrice() {

       List<WebElement> cartItems = cart.getCartProducts();

       for(WebElement cartItem : cartItems) {
           int i = products.indexOf(cartItem.findElement(By.cssSelector(".cartSection h3")).getText());
           Assert.assertEquals(cartItem.findElement(By.cssSelector(".cartSection h3")).getText(),products.get(i));
           Assert.assertEquals(cartItem.findElement(By.cssSelector(".cartSection p:nth-child(4)")).getText(), "MRP $ " + prices.get(products.get(i).split(" ")[0].toLowerCase()));
           Assert.assertEquals(cartItem.findElement(By.cssSelector(".cartSection p:nth-child(1)")).getText(), "$ " + prices.get(products.get(i).split(" ")[0].toLowerCase()));
           Assert.assertEquals(cart.getSubTotal(),"$"+ totalPrice);
           Assert.assertEquals(cart.getTotal(),"$"+ totalPrice);
       }

    }

    @When("User adds products to cart and clicks on checkout")
    public void userAddsProductsToCartAndClicksOnCheckout() {

        prod = prod.gotoProductsPage();

        products.add("ZARA COAT 3");
        products.add("QWERTY");
        for (String product : products) {
            prod.addProductToCart(product);
        }
        cart = prod.gotoCart();
        cart.checkout();
    }

    @Then("Checkout page is displayed")
    public void checkoutPageIsDisplayed() {
        Assert.assertTrue(driver.getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/order?prop="));
    }

    @When("User adds products to cart and clicks on delete")
    public void userAddsProductsToCartAndClicksOnDelete() throws InterruptedException {
        prod = prod.gotoProductsPage();

        products.add("ZARA COAT 3");
        products.add("QWERTY");
        for (String product : products) {
            prod.addProductToCart(product);
            totalPrice += prices.get(product.split(" ")[0].toLowerCase());
        }
        cart = prod.gotoCart();

        List<WebElement> cartItems = cart.getCartProducts();

        for(WebElement cartItem : cartItems){
            if(cartItem.findElement(By.cssSelector(".cartSection h3")).getText().equals("QWERTY")){
                cartItem.findElement(By.cssSelector(".btn-danger")).click();
                break;
            }
        }

        Thread.sleep(1000);

    }

    @Then("product is removed from cart")
    public void productIsRemovedFromCart() {
        Assert.assertEquals(cart.getCartCount(),products.size()-1);

        List<WebElement> cartItems = cart.getCartProducts();

        for(WebElement cartItem : cartItems){
            Assert.assertNotEquals(cartItem.findElement(By.cssSelector(".cartSection h3")).getText(), "QWERTY");
            totalPrice += prices.get(cartItem.findElement(By.cssSelector(".cartSection h3")).getText().split(" ")[0].toLowerCase());
        }

        Assert.assertEquals(cart.getSubTotal(),"$"+ totalPrice);
        Assert.assertEquals(cart.getTotal(),"$"+ totalPrice);

    }


    @When("User adds products to cart and clicks on buy now")
    public void userAddsProductsToCartAndClicksOnBuyNow() {

        prod = prod.gotoProductsPage();

        products.add("ZARA COAT 3");
        products.add("QWERTY");
        for (String product : products) {
            prod.addProductToCart(product);
        }
        cart = prod.gotoCart();

        List<WebElement> cartItems = cart.getCartProducts();
        int i = 0;
        for(WebElement cartItem : cartItems){
            cartItems = cart.getCartProducts();
            cartItems.get(i).findElement(By.cssSelector(".btn-primary")).click();
            Assert.assertTrue(driver.getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/order?prop="));
            prod.gotoCart();
        }

    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.close();
    }
}
