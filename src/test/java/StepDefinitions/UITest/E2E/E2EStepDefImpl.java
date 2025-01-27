package StepDefinitions.UITest.E2E;

import BaseTest.BaseTest;
import PageObjects.*;
import com.github.javafaker.Faker;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class E2EStepDefImpl extends BaseTest {

    Faker faker = new Faker();

    private LoginPage login;
    private RegisterPage register;
    private ForgotPasswordPage forgotPassword;
    private ProductsPage productsPage;
    private ProductPage productPage;
    private CartPage cartPage;
    private OrdersPage ordersPage;
    private CheckoutPage checkoutPage;
    private OrderConfirmationPage orderConfirmationPage;
    private OrderPage viewOrder;
    private String email, password, fName, lName, updatedPassword, prodName, prodPrice, orderId;
    private List<WebElement> products, cartProducts, orderIds, orders;
    private int noOfProductsToAddToCart = 3;


    @Given("User landed on ECommerce and navigates to register page")
    public void userLandedOnECommerceAndNavigatesToRegisterPage() throws IOException {
        //Launching application and navigating to register page
        register = launchApplication().goToRegisterPage();
    }

    @When("User provides all valid details in registration form and submits the form")
    public void userProvidesAllValidDetailsInRegistrationFormAndSubmitsTheForm() {

        //Generating fake data
        email = faker.internet().emailAddress();
        password = "151Fa04124@4517";
        fName = faker.name().firstName();
        lName = faker.name().lastName();

        //Registering user with all details
        register.registerUser(fName, lName, email, "9063517617", "Doctor", "male", password, password, true);


    }

    @Then("{string} account created message is displayed")
    public void accountCreatedMessageIsDisplayed(String message) throws InterruptedException {

        //Validating success registration message
        Assert.assertEquals(register.getSuccessMessage(), message);

        int row = excelUtils.getRowCount();
        excelUtils.writeCell(row, 0, email);
        excelUtils.writeCell(row, 1, password);
        excelUtils.save("Data.xlsx");
    }

    @When("User clicks on login button")
    public void userClicksOnLoginButton() {
        //Navigating to login page
        login = register.gotoLoginAfterRegister();
    }

    @Then("User is redirected to login page")
    public void userIsRedirectedToLoginPage() throws InterruptedException {
        //Validating user is on login page
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/login");
        Thread.sleep(2000);
    }

    @When("User Clicks on forgot password link")
    public void userClicksOnForgotPasswordLink() {
        //Clicking on forgot password link
        forgotPassword = login.goToForgotPassword();
    }

    @Then("User is redirected to forgot password page")
    public void userIsRedirectedToForgotPasswordPage() {
        //Validating user is on forgot password page
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/password-new");
    }

    @When("User provides registered email and new password and clicks on submit button")
    public void userProvidesRegisteredEmailAndNewPasswordAndClicksOnSubmitButton() {
        //User provides email and updated password and submits forgot password form
        updatedPassword = faker.internet().password();

        //Filling forgot password form and submitting form
        login = forgotPassword.updatePassword(email, updatedPassword, updatedPassword);

    }

    @Then("{string} message is displayed and user is redirected to login page")
    public void messageIsDisplayedAndUserIsRedirectedToLoginPage(String message) throws InterruptedException {
        //Validating success message and user is redirected to login page
        Assert.assertEquals(login.getPasswordUpdateText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/login");
        int row = excelUtils.getRowCount() - 1;
        excelUtils.writeCell(row, 0, email);
        excelUtils.writeCell(row, 1, updatedPassword);
        excelUtils.save("Data.xlsx");
        Thread.sleep(2000);
    }

    @When("User provides email and updates password in login form and submits the form")
    public void userProvidesEmailAndUpdatesPasswordInLoginFormAndSubmitsTheForm() {

        //Providing email and updates password in login form and submitting form
        productsPage = login.loginApplication(email, updatedPassword);


    }

    @Then("{string} message is displayed and products are displayed")
    public void messageIsDisplayedAndProductsAreDisplayed(String message) throws InterruptedException {
        //Validating success message and products are displayed
        Assert.assertEquals(login.getSuccessText(), message);
        Assert.assertFalse(productsPage.getProductList().isEmpty());
        //Extracting products
        products = productsPage.getProductList();
    }

    @When("User clicks on view button on product")
    public void userClicksOnViewButtonOnProduct() throws InterruptedException {
        //Clicking on view button on product
        prodName = products.get(0).findElement(By.cssSelector(".card-body h5 b")).getText();
        prodPrice = products.get(0).findElement(By.cssSelector(".card-body div div")).getText();
        productPage = productsPage.viewProductDetails(prodName);
        Thread.sleep(2000);
    }

    @Then("Product details are displayed")
    public void productDetailsAreDisplayed() {
        //Validating product details and url
        Assert.assertEquals(productPage.getProductName(), prodName);
        Assert.assertEquals(productPage.getProductPrice(), prodPrice);
        Assert.assertTrue(driver.getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/product-details/6581cade9fd99c85e8ee7ff5"));
    }

    @When("User clicks on continue shopping button")
    public void userClicksOnContinueShoppingButton() {
        //Clicking on continue shopping button
        productsPage = productsPage.gotoProductsPage();
    }

    @Then("User is redirected to products page")
    public void userIsRedirectedToProductsPage() throws InterruptedException {
        //Validating user is on products page
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/dash");
        //Extracting products in the product page
        products = productsPage.getProductList();
    }

    @When("User clicks on add to cart button on product")
    public void userClicksOnAddToCartButtonOnProduct() throws InterruptedException {
        //Adding products to cart
        int cartCount;
        try {
            cartCount = Integer.parseInt(productsPage.getCartCount()); // Attempt to parse
        } catch (NumberFormatException e) {
            cartCount = 0; // Default to 0 if parsing fails
        }

        while (cartCount < noOfProductsToAddToCart) {
            //Generating a random index to get product from products list
            int index = new Random().nextInt(products.size());
            productsPage.addProductToCart(products.get(index).findElement(By.cssSelector(".card-body h5 b")).getText());

            cartCount = Integer.parseInt(productsPage.getCartCount());
        }
    }

    @Then("Product is added to cart")
    public void productIsAddedToCart() {
        //Validating product is added to cart
        Assert.assertTrue(productsPage.getCartCount().contains(String.valueOf(noOfProductsToAddToCart)));
    }

    @When("User clicks on cart icon")
    public void userClicksOnCartIcon() throws InterruptedException {
        //Clicking on cart icon
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
        Thread.sleep(4000);
        cartPage = productsPage.gotoCart();
    }

    @Then("Cart page is displayed to user along with cart products")
    public void cartPageIsDisplayedToUserAlongWithCartProducts() {
        //Validating cart page is displayed with products
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/cart");
        Assert.assertFalse(cartPage.getCartProducts().isEmpty());
        //Extracting cart products
        cartProducts = cartPage.getCartProducts();
    }

    @When("User clicks on delete button on cart product")
    public void userClicksOnDeleteButtonOnCartProduct() throws InterruptedException {
        //Generating random index to delete from cart
        int index = new Random().nextInt(cartProducts.size());
        //Removing the product from cart
        cartProducts.get(index).findElement(By.cssSelector(".btn-danger")).click();
        Thread.sleep(2000);

    }

    @Then("Product is deleted from cart")
    public void productIsDeletedFromCart() {
        //Validating updated cart count
        Assert.assertEquals(Integer.parseInt(productsPage.getCartCount()), noOfProductsToAddToCart - 1);
        //Extracting updated cart products
        cartProducts = cartPage.getCartProducts();
    }

    @When("User clicks on checkout button on cart page")
    public void userClicksOnCheckoutButtonOnCartPage() throws InterruptedException {
        //Clicking on checkout button
        checkoutPage = cartPage.checkout();
    }

    @Then("User is redirected to checkout page")
    public void userIsRedirectedToCheckoutPage() {
        //Validating checkout page is displayed and no of products in checkout page
//        Assert.assertEquals(checkoutPage.getProductCount(), noOfProductsToAddToCart - 1);
        Assert.assertTrue(driver.getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/order?prop="));
    }

    @When("User provides all valid details in checkout form and submits the form")
    public void userProvidesAllValidDetailsInCheckoutFormAndSubmitsTheForm() {
        //Providing all valid details in checkout form and submitting form
        checkoutPage.selectCountry("ind", "India");
        orderConfirmationPage = checkoutPage.clickSubmitButton();
    }

    @Then("{string} message is displayed and order details are displayed")
    public void messageIsDisplayedAndOrderDetailsAreDisplayed(String message) {
        //Validating success message and order details are displayed
        Assert.assertEquals(orderConfirmationPage.getSuccessMessage(), message);
        Assert.assertEquals(orderConfirmationPage.getOrderDetails().size(), noOfProductsToAddToCart - 1);
        Assert.assertEquals(orderConfirmationPage.getOrderIds().size(), noOfProductsToAddToCart - 1);
        //Extracting order ids
        orderIds = orderConfirmationPage.getOrderIds();
    }

    @When("User clicks on order history link")
    public void userClicksOnOrderHistoryLink() {
        //Clicking on order history link
        ordersPage = orderConfirmationPage.clickOrdersLink();
    }

    @Then("User is redirected to order history page")
    public void userIsRedirectedToOrderHistoryPage() {
        //Validating order history page is displayed and order details are displayed
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/dashboard/myorders");
        Assert.assertFalse(ordersPage.getorders().isEmpty());
        //Extracting order details
        orders = ordersPage.getorders();

    }

    @When("User clicks on view order button on order history")
    public void userClicksOnViewOrderButtonOnOrderHistory() throws InterruptedException {
        //Generating random index to view order details
        int index = new Random().nextInt(orders.size());

        WebElement order = orders.get(index);
        WebElement orderIdElm = order.findElement(By.cssSelector("tr.ng-star-inserted th"));
        orderId = orderIdElm.getText();
        prodName = order.findElement(By.cssSelector("tr.ng-star-inserted td:nth-child(3)")).getText();
        prodPrice = order.findElement(By.cssSelector("tr.ng-star-inserted td:nth-child(4)")).getText();
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", orderIdElm);

        //Clicking on view order button
        order.findElement(By.cssSelector(".btn-primary")).click();
        viewOrder = new OrderPage(driver);

        Thread.sleep(2000);
    }

    @Then("Order details are displayed")
    public void orderDetailsAreDisplayed() {
        //Validating order details are displayed
        Assert.assertEquals(viewOrder.orderID(), orderId);
        Assert.assertEquals(viewOrder.productTitle(), prodName);
        Assert.assertEquals(viewOrder.productPrice(), prodPrice);
        //Going back to orders page
        ordersPage = viewOrder.gotorders();
        orders = ordersPage.getorders();
    }

    @When("User clicks on delete button on order details")
    public void userClicksOnDeleteButtonOnOrderDetails() throws InterruptedException {
        //Generating random index to delete order details
        int index = new Random().nextInt(orders.size());

        //Scrolling to order
        WebElement order = orders.get(index);
        WebElement prodName = order.findElement(By.cssSelector("tr.ng-star-inserted td:nth-child(3)"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", prodName);

        //Clicking on delete button
        order.findElement(By.cssSelector(".btn-danger")).click();

    }


    @Then("order is deleted and {string} message is displayed")
    public void orderIsDeletedAndMessageIsDisplayed(String message) {
        //Validating order is deleted
        Assert.assertEquals(ordersPage.getToastText(), message);
    }

    @When("User clicks on logout button")
    public void userClicksOnLogoutButton() {
        //Clicking on logout button
        login = productsPage.signout();
    }

    @Then("{string} message is displayed and login page is displayed to user")
    public void messageIsDisplayedAndLoginPageIsDisplayedToUser(String message) {
        //Validating logout message and login page is displayed
        Assert.assertEquals(login.getLogoutText(), message);
        Assert.assertEquals(driver.getCurrentUrl(), "https://rahulshettyacademy.com/client/auth/login");
    }

    @After
    public void afterScenario() {
        if (driver != null)
            driver.quit();
        close();
    }
}
