package PageObjects;

import Util.Utility;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class OrdersPage extends Utility {

    WebDriver driver;
    @FindBy(css = "button[routerlink='/dashboard']")
    private WebElement goBackToShopButton;

    @FindBy(xpath = "//button[normalize-space()='Go Back to Cart']")
    private WebElement goBackToCartButton;

    @FindBy(css = "tr.ng-star-inserted")
    private List<WebElement> orderDetails;

    @FindBy(css = "div.mt-4")
    private WebElement noOrdersText;

    @FindBy(css = "div[role='alert']")
    private WebElement toast;

    @FindBy(css = "div[aria-label='Unknown error occured']")
    private WebElement unknownErrorToast;

    public OrdersPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean goBackToShopButtonisVisible() {
        return goBackToShopButton.isDisplayed();
    }

    public ProductsPage gotoProductPage() throws InterruptedException {
        Thread.sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", goBackToShopButton);
        Thread.sleep(1000);
        goBackToShopButton.click();
        return new ProductsPage(driver);
    }

    public CartPage gotoCartPage() throws InterruptedException {
        Thread.sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", goBackToCartButton);
        Thread.sleep(1000);
        goBackToCartButton.click();
        return new CartPage(driver);
    }

    public List<WebElement> getorders() {
        return orderDetails;
    }

    public String getNoOrdersText() {
        return noOrdersText.getText();
    }

    public String getToastText() {
        waitForWebElementToAppear(toast);
        return toast.getText();
    }

    public String getUnknownError() {
        waitForWebElementToAppear(unknownErrorToast);
        return unknownErrorToast.getText();
    }

}
