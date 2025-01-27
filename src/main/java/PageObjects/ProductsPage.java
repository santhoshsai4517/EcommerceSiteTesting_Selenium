package PageObjects;

import Util.Utility;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ProductsPage extends Utility {

    WebDriver driver;
    By viewProduct = By.cssSelector(".w-40");
    By productsBy = By.cssSelector(".mb-3");
    By addToCart = By.cssSelector(".card-body button:last-of-type");
    By toastContainer = By.id("toast-container");
    @FindBy(css = ".ng-animating")
    private WebElement animation;
    @FindBy(tagName = "h3")
    private WebElement title;
    @FindBy(css = "button[routerlink='/dashboard/']")
    private WebElement homeButton;
    @FindBy(css = "button[routerlink*='myorders']")
    private WebElement myOrdersButton;
    @FindBy(css = "button[routerlink*='/cart']")
    private WebElement CartButton;
    @FindBy(xpath = "//button[normalize-space()='Sign Out']")
    private WebElement signOutButton;
    @FindBy(id = "res")
    private WebElement productsCount;
    @FindBy(className = "card")
    private List<WebElement> products;
    @FindBy(css = "button[class='btn btn-custom'] label")
    private WebElement cartCount;
    @FindBy(css = "div[aria-label='Product Added To Cart']")
    private WebElement ProductAdded;
    @FindBy(xpath = "(//input[@placeholder='search'])[2]")
    private WebElement searchBox;
    @FindBy(xpath = "(//input[@placeholder='Min Price'])[2]")
    private WebElement minPriceBox;
    @FindBy(xpath = "(//input[@placeholder='Max Price'])[2]")
    private WebElement maxPriceBox;
    @FindBy(css = "body > app-root:nth-child(1) > app-dashboard:nth-child(2) > section:nth-child(4) > form:nth-child(3) > div:nth-child(3) > div:nth-child(3) > input:nth-child(1)")
    private WebElement fashionFilter;
    @FindBy(css = "body > app-root:nth-child(1) > app-dashboard:nth-child(2) > section:nth-child(4) > form:nth-child(3) > div:nth-child(3) > div:nth-child(4) > input:nth-child(1)")
    private WebElement electronicsFilter;
    @FindBy(css = "body > app-root:nth-child(1) > app-dashboard:nth-child(2) > section:nth-child(4) > form:nth-child(3) > div:nth-child(3) > div:nth-child(5) > input:nth-child(1)")
    private WebElement householdFilter;
    @FindBy(css = "body > app-root:nth-child(1) > app-dashboard:nth-child(2) > section:nth-child(4) > form:nth-child(3) > div:nth-child(4) > div:nth-child(3) > input:nth-child(1)")
    private WebElement tshirtFilter;
    @FindBy(css = "body > app-root:nth-child(1) > app-dashboard:nth-child(2) > section:nth-child(4) > form:nth-child(3) > div:nth-child(4) > div:nth-child(4) > input:nth-child(1)")
    private WebElement shirtFilter;
    @FindBy(css = "body > app-root:nth-child(1) > app-dashboard:nth-child(2) > section:nth-child(4) > form:nth-child(3) > div:nth-child(4) > div:nth-child(5) > input:nth-child(1)")
    private WebElement shoesFilter;
    @FindBy(css = "body > app-root:nth-child(1) > app-dashboard:nth-child(2) > section:nth-child(4) > form:nth-child(3) > div:nth-child(4) > div:nth-child(6) > input:nth-child(1)")
    private WebElement mobilesFilter;
    @FindBy(css = "body > app-root:nth-child(1) > app-dashboard:nth-child(2) > section:nth-child(4) > form:nth-child(3) > div:nth-child(4) > div:nth-child(7) > input:nth-child(1)")
    private WebElement laptopFilter;
    @FindBy(css = "body > app-root:nth-child(1) > app-dashboard:nth-child(2) > section:nth-child(4) > form:nth-child(3) > div:nth-child(5) > div:nth-child(3) > input:nth-child(1)")
    private WebElement menFilter;
    @FindBy(css = "body > app-root:nth-child(1) > app-dashboard:nth-child(2) > section:nth-child(4) > form:nth-child(3) > div:nth-child(5) > div:nth-child(4) > input:nth-child(1)")
    private WebElement womenFilter;
    @FindBy(css = "div[aria-label='No Products Found']")
    private WebElement noProductsToast;
    @FindBy(className = "left")
    private WebElement logo;

    public ProductsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public List<WebElement> getProductList() throws InterruptedException {
//        waitForWebElementToAppear(productsCount);
//		System.out.println(products.size());
        Thread.sleep(1000);
        return products;
    }

    public WebElement getProductByName(String productName) throws InterruptedException {
        return getProductList().stream().filter(p -> p.findElement(By.tagName("b")).getText().equals(productName))
                .findFirst().orElse(null);
    }

    public String addProductToCart(String productName) throws InterruptedException {

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getProductByName(productName).findElement(addToCart));
        Thread.sleep(2000);
        getProductByName(productName).findElement(addToCart).click();
        waitForElementToAppear(toastContainer);
        try {
            String text = ProductAdded.getText();
            waitForElementToDisappear(animation);
            return text;
        } catch (Exception e) {
            return "";
        }

    }

    public String getTitleText() {
        return title.getText();
    }

    public ProductsPage clickLogo() {
        logo.click();
        return new ProductsPage(driver);
    }

    public LoginPage signout() {
        signOutButton.click();
        return new LoginPage(driver);
    }

    public OrdersPage gotoMyOrders() {
        myOrdersButton.click();
        return new OrdersPage(driver);
    }

    public CartPage gotoCart() {
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", CartButton);

        CartButton.click();
        return new CartPage(driver);
    }

    public ProductsPage gotoProductsPage() {
        homeButton.click();
        return new ProductsPage(driver);
    }

    public String getProductCount() {
        return productsCount.getText();
    }

    public String getCartCount() {
        return cartCount.getText();
    }

    public void searchProduct(String prodName) {
        searchBox.sendKeys(prodName, Keys.ENTER);
    }

    public void minPrice(String min) {
        minPriceBox.sendKeys(min, Keys.ENTER);
    }

    public void maxPrice(String max) {
        maxPriceBox.sendKeys(max, Keys.ENTER);
    }

    public void applyFashionFilter() {
        fashionFilter.click();
    }

    public void applyElectronisFilter() {
        electronicsFilter.click();
    }

    public void applyHouseholdFilter() {
        householdFilter.click();
    }

    public void applyTshirtFilter() {
        tshirtFilter.click();
    }

    public void applyShirtFilter() {
        shirtFilter.click();
    }

    public void applyShoesFilter() {
        shoesFilter.click();
    }

    public void applyMobileFilter() {
        mobilesFilter.click();
    }

    public void applylaptopFilter() {
        laptopFilter.click();
    }

    public void applyMenFilter() {
        menFilter.click();
    }

    public void applyWomenFilter() {
        womenFilter.click();
    }

    public String getNoProductsError() {
        waitForWebElementToAppear(noProductsToast);
        return noProductsToast.getText();
    }

    public ProductPage viewProductDetails(String productName) throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getProductByName(productName).findElement(viewProduct));
        getProductByName(productName).findElement(viewProduct).click();
        return new ProductPage(driver);
    }

}
