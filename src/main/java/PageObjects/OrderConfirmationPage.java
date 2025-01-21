package PageObjects;


import Util.Utility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class OrderConfirmationPage extends Utility {

    WebDriver driver;

    @FindBy(css = "tr button")
    private List<WebElement> downloadButtons;

    @FindBy(css = "label[routerlink='/dashboard/myorders']")
    private WebElement ordersLink;

    @FindBy(css = "label.ng-star-inserted")
    private List<WebElement> orderIds;

    @FindBy(css = "div[class='ng-star-inserted'] tr")
    private List<WebElement> orderDetails;

    @FindBy(css = "div[aria-label='Order Placed Successfully']")
    private WebElement successToast;

    public OrderConfirmationPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public List<WebElement> getDownloadButtons() {
        return downloadButtons;
    }

    public List<WebElement> getOrderIds() {
        return orderIds;
    }

    public List<WebElement> getOrderDetails() {
        return orderDetails;
    }

    public OrdersPage clickOrdersLink() {
        ordersLink.click();
        return new OrdersPage(driver);
    }

    public String getSuccessMessage() {
        return successToast.getText();
    }
}
