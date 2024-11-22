package PageObjects;

import Util.Utility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class OrderPage extends Utility {

    WebDriver driver;

    @FindBy(css = ".btn.-teal")
    private WebElement viewOrders;

    @FindBy(css = ".col-text")
    private WebElement orderIDElm;

    @FindBy(css = ".title")
    private WebElement prodTitleElm;

    @FindBy(css = ".price")
    private WebElement prodPriceElm;

    @FindBy(css = ".text")
    private List<WebElement> addressElms;

    @FindBy(css = "div[aria-label='Unknown error occured']")
    private WebElement unknownErrorToast;

    @FindBy(css = ".blink_me")
    private WebElement errorText;

    public OrderPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public OrdersPage gotorders() {
        viewOrders.click();
        return new OrdersPage(driver);
    }

    public String orderID() {
        return orderIDElm.getText();
    }

    public String productTitle() {
        return prodTitleElm.getText();
    }

    public String productPrice() {
        return prodPriceElm.getText();
    }

    public List<WebElement> getAddressElms() {
        return addressElms;
    }

    public String getUnknownError() {
        waitForWebElementToAppear(unknownErrorToast);
        return unknownErrorToast.getText();
    }

    public String getErrorText() {
        waitForWebElementToAppear(errorText);
        return errorText.getText();
    }
}
