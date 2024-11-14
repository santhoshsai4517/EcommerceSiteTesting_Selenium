package PageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Utility.Utility;

import java.util.List;

public class CartPage extends Utility {

	WebDriver driver;

	public CartPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath = "//h1[normalize-space()='My Cart']")
	private WebElement cartText;

	@FindBy(css = "#toast-container")
	private WebElement noProductsToast;

	@FindBy(css= "button[routerlink='/dashboard']")
	private WebElement continueShoppingBtn;

	@FindBy(css ="div[class='ng-star-inserted'] h1")
	private WebElement noProductsText;

	@FindBy(className = "cartWrap")
	private List<WebElement> cartItems;

	@FindBy(css = "li:nth-child(1) span:nth-child(2)")
	private WebElement subTotal;

	@FindBy(css = "li:nth-child(2) span:nth-child(2)")
	private WebElement total;

	@FindBy(css=".totalRow button")
	private WebElement checkoutBtn;

	public String getCartText() {
		return cartText.getText();
	}

	public String getNoProductsToastText() {
		return noProductsToast.getText();
	}

	public ProductsPage continueShopping(){
		continueShoppingBtn.click();
        return new ProductsPage(driver);
	}

	public String getNoProductsText() {
        return noProductsText.getText();
    }

	public int getCartCount(){
		return cartItems.size();
	}

	public List<WebElement> getCartProducts(){
		return cartItems;
	}

	public String getSubTotal(){
        return subTotal.getText();
    }

	public String getTotal(){
        return total.getText();
    }

	public CheckoutPage checkout(){
        checkoutBtn.click();
        return new CheckoutPage(driver);
    }
	
}
