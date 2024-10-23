package PageObjects;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utility.Utility;

public class LoginPage extends Utility {

	WebDriver driver;
	
		
	
	public LoginPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath = "//section/h3")
	private WebElement text;
	
	@FindBy(xpath = "//a[@class='btn1']")
	private WebElement registerButton;
	
	@FindBy(className = "forgot-password-link")
	private WebElement forgotPasswordLink;
	
	@FindBy(className = "text-reset")
	private WebElement registerLink;
	
	@FindBy(id="userEmail")
	private WebElement userEmail;
	
	@FindBy(id="userPassword")
	private WebElement password;
	
	@FindBy(id="login")
	private WebElement submit;
	
	@FindBy(className = "toast-message")
	private WebElement errorToast;
	
	@FindBy(className = "toast-title")
	private WebElement successToast;
	
	public ProductsPage loginApplication(String email,String pass) {
		userEmail.sendKeys(email);
		password.sendKeys(pass);
		submit.click();
		return new ProductsPage(driver);
	}
	
	public String getLoginPageText() {
		return text.getText();
	}
	
	public void goTo() {
		driver.get("https://rahulshettyacademy.com/client/");
	}
	
	public RegisterPage goToRegisterPage() {
		registerButton.click();
		return new RegisterPage(driver); 
	}
	
	public ForgotPasswordPage goToForgotPassword() {
		forgotPasswordLink.click();
		return new ForgotPasswordPage(driver);
	}
	
	public String getErrorText() {
		waitForWebElementToAppear(errorToast);
		return errorToast.getText();
		
	}

	public RegisterPage goToRegisterPageLink() {
		registerLink.click();
		return new RegisterPage(driver);
	}
	
	public String getSuccessText() {
		waitForWebElementToAppear(successToast);
		return successToast.getText();
	}
}

