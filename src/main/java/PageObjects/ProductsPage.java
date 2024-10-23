package PageObjects;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utility.Utility;


public class ProductsPage extends Utility {

	WebDriver driver;

	public ProductsPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = ".mb-3")
	private List<WebElement> products;
	
	@FindBy(css = ".ng-animating")
	private WebElement animation;
	
	@FindBy(tagName = "h3")
	private WebElement title;
	

	
	By productsBy = By.cssSelector(".mb-3");
	By addToCart = By.cssSelector(".card-body button:last-of-type");
	By toastContainer = By.id("toast-container");

	public List<WebElement> getProductList() {
		waitForElementToAppear(productsBy);
		return products;
	}
	
	public WebElement getProductByName(String productName) {
		return getProductList().stream().filter(p -> p.findElement(By.tagName("b")).getText().equals(productName)).findFirst().orElse(null);
	}
	
	public void addProductToCart(String productName) {
		getProductByName(productName).findElement(addToCart).click();
		waitForElementToAppear(toastContainer);
		waitForElementToDisappear(animation);
	}

	public String getTitleText() {
		return title.getText();
	}
	
	

	
}
