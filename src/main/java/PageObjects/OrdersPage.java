package PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utility.Utility;

public class OrdersPage extends Utility {

	WebDriver driver;

	public OrdersPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(css = "button[routerlink='/dashboard/']")
	private WebElement goBackToShopButton;
	
	public boolean goBackToShopButtonisVisible() {
		return goBackToShopButton.isDisplayed();
	}
	
}
