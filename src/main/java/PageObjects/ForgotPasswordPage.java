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

	@FindBy(css = "input[type='email']")
	private WebElement emailEditBox;

	@FindBy(id = "userPassword")
	private WebElement passwordEditBox;

	@FindBy(id = "confirmPassword")
	private WebElement confirmPasswordEditBox;

	@FindBy(tagName = "button")
	private WebElement submitBtn;

	@FindBy(css = "a[href*='login']")
	private WebElement loginLink;

	@FindBy(css = "a[href*='register']")
	private WebElement registerLink;

	@FindBy(xpath = "//div[contains(text(),'*Email is required')]")
	private WebElement emailError;

	@FindBy(xpath = "//div[contains(text(),'*Password is required')]")
	private WebElement passwordError;

	@FindBy(xpath = "//div[contains(text(),'*Confirm Password is required')]")
	private WebElement confirmPasswordError;

	@FindBy(xpath = "//div[contains(text(),'*Enter Valid Email')]")
	private WebElement emailInavlidError;

	@FindBy(xpath = "//div[contains(text(),'Password and Confirm Password must match with each other.')]")
	private WebElement passwordMismatchError;

	@FindBy(css = "div[role='alert']")
	private WebElement userNotFoundError;

	@FindBy(xpath = "//div[contains(text(),'Unknown error occured')]")
	private WebElement unknownError;

	public String getTitleText() {
		return title.getText();
	}

	public RegisterPage clickRegisterLink() {
		registerLink.click();
		return new RegisterPage(driver);
	}

	public LoginPage clickLoginLink() {
		loginLink.click();
		return new LoginPage(driver);
	}

	public LoginPage updatePassword(String email, String password, String confirmPassword) {
		emailEditBox.sendKeys(email);
		passwordEditBox.sendKeys(password);
		confirmPasswordEditBox.sendKeys(confirmPassword);
		submitBtn.click();
		return new LoginPage(driver);
	}

	public String getEmailError() {
		return emailError.getText();
	}

	public String getPasswordError() {
		return passwordError.getText();
	}

	public String getConfirmPasswordErrpr() {
		return confirmPasswordError.getText();
	}

	public String getInvalidEmailError() {
		return emailInavlidError.getText();
	}

	public String getPasswordMismatchError() {
		waitForWebElementToAppear(passwordMismatchError);
		return passwordMismatchError.getText();
	}

	public String getUserNotFoundError() {
		waitForWebElementToAppear(userNotFoundError);
		return userNotFoundError.getText();
	}

	public String getUnknownError() {
		waitForWebElementToAppear(unknownError);
		return unknownError.getText();
	}

}
