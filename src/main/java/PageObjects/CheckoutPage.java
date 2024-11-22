package PageObjects;

import Util.Utility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class CheckoutPage extends Utility {
    WebDriver driver;
    @FindBy(css = ".item__details")
    private List<WebElement> productDetails;

    @FindBy(css = ".item__title")
    private WebElement productTitle;

    @FindBy(css = ".item__price")
    private WebElement productPrice;

    @FindBy(css = "input[placeholder='Select Country']")
    private WebElement country;

    @FindBy(css = "section span.ng-star-inserted")
    private List<WebElement> contries;

    @FindBy(css = ".action__submit")
    private WebElement submitButton;

    @FindBy(css = "div[aria-label='Please Enter Full Shipping Information']")
    private WebElement errorToast;

    @FindBy(css = "div[aria-label='Unknown error occured']")
    private WebElement unknownErrorToast;

    public CheckoutPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public List<WebElement> getProductDetails() {
        return productDetails;
    }

    public int getProductCount() {
        return productDetails.size();
    }

    public String getProductName() {
        return productTitle.getText();
    }

    public String getProductPrice() {
        return productPrice.getText();
    }

    public void selectCountry(String countryCode, String countryName) {
        country.sendKeys(countryCode);
        contries.stream().filter(c -> c.getText().equalsIgnoreCase(countryName)).findFirst().ifPresent(WebElement::click);
    }

    public OrderConfirmationPage clickSubmitButton() {
        submitButton.click();
        return new OrderConfirmationPage(driver);
    }

    public String getErrorText() {
        waitForWebElementToAppear(errorToast);
        return errorToast.getText();
    }

    public String getUnknownError() {
        waitForWebElementToAppear(unknownErrorToast);
        return unknownErrorToast.getText();
    }

}
