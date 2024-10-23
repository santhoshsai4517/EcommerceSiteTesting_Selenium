package PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utility.Utility;

public class ForgotPasswordPage extends Utility {

	WebDriver driver;

	ForgotPasswordPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(className = "card-title")
	private WebElement title;
	
	
	public String getTitleText() {
		return title.getText();
	}
	

}
