package PageObjects;

import Util.Utility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductPage extends Utility {
    WebDriver driver;
    @FindBy(css = ".btn-primary")
    private WebElement addToCartButton;

    @FindBy(css = ".continue")
    private WebElement continueShoppingButton;

    @FindBy(tagName = "h2")
    private WebElement productName;

    @FindBy(css = ".row div h3")
    private WebElement productPrice;

    @FindBy(css = "div[role='alert']")
    private WebElement productAddedToCartToast;

    public ProductPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void addToCart() {
        addToCartButton.click();
    }

    public ProductsPage continueShopping() {
        continueShoppingButton.click();
        return new ProductsPage(driver);
    }

    public String getProductName() {
        return productName.getText();
    }

    public String getProductPrice() {
        return productPrice.getText();
    }

    public String getProductAddedToCartToastText() {
        waitForWebElementToAppear(productAddedToCartToast);
        return productAddedToCartToast.getText();
    }

}
