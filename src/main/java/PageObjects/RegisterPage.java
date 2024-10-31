package PageObjects;

import java.util.List;

import javax.xml.xpath.XPath;

import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import Utility.Utility;

public class RegisterPage extends Utility {

	WebDriver driver;

	public RegisterPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(className = "login-title")
	private WebElement registerText;

	@FindBy(id = "firstName")
	private WebElement firstNameEditBox;

	@FindBy(id = "lastName")
	private WebElement lastNameEditBox;

	@FindBy(id = "userEmail")
	private WebElement emailEditBox;

	@FindBy(id = "userMobile")
	private WebElement mobileEditBox;

	@FindBy(tagName = "select")
	private WebElement OccupationDropdown;

	@FindBy(css = "input[type='radio']")
	private List<WebElement> genderRadio;

	@FindBy(id = "userPassword")
	private WebElement passwordEditBox;

	@FindBy(id = "confirmPassword")
	private WebElement confirmPasswordEditBox;

	@FindBy(css = "input[type='checkbox']")
	private WebElement ageCheckBox;

	@FindBy(id = "login")
	private WebElement registerButton;

	@FindBy(className = "text-reset")
	private WebElement loginLink;
	
	@FindBy(xpath = "//h1[normalize-space()='Account Created Successfully']")
	private WebElement successMsg;
	
	@FindBy(className = "btn")
	private WebElement loginBtn;
	
	@FindBy(xpath = "//div[@aria-label='Registered Successfully']")
	private WebElement registerSuccessToast;
	
	@FindBy(xpath =  "//div[contains(text(),'*Enter Valid Email')]")
	private WebElement invalidEmailError;
	
	@FindBy(xpath = "//div[contains(text(),'*Phone Number must be 10 digit')]")
	private WebElement phoneNumber10Digitsl;
	
	@FindBy(xpath = "//div[contains(text(),'*only numbers is allowed')]")
	private WebElement phonNumberWithChars;
	
	@FindBy(xpath = "//div[contains(text(),'Password and Confirm Password must match with each other.')]")
	private WebElement passwordMismatchError;
	
	@FindBy(css = "div[role='alert']")
	private WebElement passwordErrorToast;
	
	@FindBy(xpath = "//div[contains(text(),'*First Name is required')]")
	private WebElement firstNameError;
	
	@FindBy(xpath = "//div[contains(text(),'*Email is required')]")
	private WebElement emailError;
	
	@FindBy(xpath = "//div[contains(text(),'*Phone Number is required')]")
	private WebElement mobileError;
	
	@FindBy(xpath = "//div[contains(text(),'*Password is required')]")
	private WebElement passwordError;
	
	@FindBy(xpath = "//div[contains(text(),'Confirm Password is required')]")
	private WebElement confirmPasswordError;
	
	@FindBy(xpath = "//div[contains(text(),'*Please check above checkbox')]")
	private WebElement checkboxError;
	
	@FindBy(css = "div[role='alert']")
	private WebElement userAlreadyExists;
	
	public String getRegisterPageText() {
		return registerText.getText();
	}

	public LoginPage gotologinPage() {
		loginLink.click();
		return new LoginPage(driver);
	}
	
	public void clickRegisterButton() {
		registerButton.click();
	}

	public void registerUser(String fName, String lName, String email, String mobile, String occupation,
			String gender, String password, String confirmPassword, boolean check) {

		firstNameEditBox.sendKeys(fName);
		lastNameEditBox.sendKeys(lName);
		emailEditBox.sendKeys(email);
		mobileEditBox.sendKeys(mobile);
		Select dropDown = new Select(OccupationDropdown);
		dropDown.selectByVisibleText(occupation);
		for (WebElement radioButton : genderRadio) {
			String value = radioButton.getAttribute("value");
			if (value.equals("Male")) {
				radioButton.click();
				break;
			}
			if (value.equals("Female")) {
				radioButton.click();
				break;
			}
		}
		passwordEditBox.sendKeys(password);
		confirmPasswordEditBox.sendKeys(confirmPassword);
		if(check)
			ageCheckBox.click();
		
		
		registerButton.click();
		
	}
	
	public LoginPage gotoLoginAfterRegister() {
		loginBtn.click();
		return new LoginPage(driver);
	}
	
	public String getInvalidEmaiLError() {
		return invalidEmailError.getText();
	}
	
	public String getPhoneNumber10DigitsError() {
		return phoneNumber10Digitsl.getText();
	}
	
	public String getPhoneNumberWithCharsError() {
		return phonNumberWithChars.getText();
	}
	
	public String getPasswordErrorToast() {
		return passwordErrorToast.getText();
	}
	
	public String getPasswordMismatchError() {
		return passwordMismatchError.getText();
	}
	
	public String getEmailError() {
		return emailError.getText();
	}
	
	public String getfirstNameError() {
		return firstNameError.getText();
	}
	
	public String getPhoneNumbnerError() {
		return mobileError.getText();
	}
	
	public String getPasswordError() {
		return passwordError.getText();
	}
	
	public String getConfirmPasswordError() {
		return confirmPasswordError.getText();
	}
	
	public String getCheckBoxError() {
		return checkboxError.getText();
	}
	
	public String getUserAlreadyExistsError() {
		waitForWebElementToAppear(userAlreadyExists);
		return userAlreadyExists.getText();
	}

	public String getSuccessMessage() {
		waitForWebElementToAppear(successMsg);
		return successMsg.getText();
	}
	
}
