package StepDefinitions.APITest.GetCartProductsAPI;

import BaseTest.BaseTest;
import POJO.Request.AddToCartAPIRequest;
import POJO.Request.GetAllProductsAPIRequest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class FunctionalityStepDefImpl extends BaseTest {

    RequestSpecification request;
    LoginAPIResponse loginAPIResponse;
    GetAllProductsAPIResponse getAllProductsAPIResponse;
    AddToCartAPI_SuccessResponse addToCartAPIResponse;
    GetCartProductsAPI_NoCartProducts getCartProductsAPIResponse;
    GetCartProductsAPI_WithCartproducts getCartProductsAPIResponseWithCartProducts;
    List<Product> products;
    int count = 2;
    List<String> cartProductIDs = new ArrayList<>();

    @Given("The user has valid credentials get cart product details")
    public void theUserHasValidCredentialsGetCartProductDetails() {

        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();
    }

    @When("The user sends a login API request get cart product details")
    public void theUserSendsALoginAPIRequestGetCartProductDetails() {
        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginAPIResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token get cart product details")
    public void theUserShouldReceiveAValidAuthenticationTokenGetCartProductDetails() {
        //Verifying token is not null
        Assert.assertNotNull(loginAPIResponse.getToken());
    }

    @When("The user sends a request to get all products using the authentication token get cart product details")
    public void theUserSendsARequestToGetAllProductsUsingTheAuthenticationTokenGetCartProductDetails() {
        //Creating request body for get all products api request
        GetAllProductsAPIRequest requestBody = new GetAllProductsAPIRequest();
        requestBody.setProductName("");
        requestBody.setMinPrice(null);
        requestBody.setMaxPrice(null);
        requestBody.setProductCategory(Collections.emptyList());
        requestBody.setProductSubCategory(Collections.emptyList());
        requestBody.setProductFor(Collections.emptyList());

        //Sending get all products api request body for get all products api request
        getAllProductsAPIResponse = given().spec(requestSpecification).body(requestBody)
                .header("Authorization", loginAPIResponse.getToken()).log().all().when()
                .post("/product/get-all-products")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetAllProductsAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, 2000, ContentType.JSON)).log().all()
                .extract()
                .as(GetAllProductsAPIResponse.class);
    }

    @Then("The user should receive a list of product IDs get cart product details")
    public void theUserShouldReceiveAListOfProductIDsGetCartProductDetails() {
        //Extracting product details
        products = getAllProductsAPIResponse.getData();
    }

    @When("The user sends a request to get cart products")
    public void theUserSendsARequestToGetCartProducts() {

        //Sending get cart count api request and extracting response as GetCartCountAPI_NoCartProductsResponse object and validating response code and response time
        getCartProductsAPIResponse = given().spec(requestSpecification)
                .header("Authorization", loginAPIResponse.getToken())
                .pathParam("userId", loginAPIResponse.getUserId()).log().all()
                .when()
                .get("user/get-cart-products/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetCartProductsAPI_NoProductsSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetCartProductsAPI_NoCartProducts.class);
    }

    @Then("{string} message is returned in api response")
    public void messageIsReturnedInApiResponse(String message) {
        //Verifying message matches
        Assert.assertEquals(message, getCartProductsAPIResponse.getMessage());
    }

    @When("User adds products to cart using add to cart api")
    public void userAddsProductsToCartUsingAddToCartApi() {
        for (int i = 0; i < count; i++) {
            AddToCartAPIRequest requestBody = new AddToCartAPIRequest();
            Product product = products.get((int) (Math.random() * products.size()));
            cartProductIDs.add(product.get_id());
            requestBody.setProduct(product);
            requestBody.set_id(loginAPIResponse.getUserId());

            //Sending add to cart api request body
            addToCartAPIResponse = given().spec(requestSpecification).body(requestBody)
                    .header("Authorization", loginAPIResponse.getToken()).log().all().when()
                    .post("/user/add-to-cart")
                    .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/AddToCartAPI_SuccessSchema.json")))
                    .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                    .extract().as(AddToCartAPI_SuccessResponse.class);
        }
    }

    @When("Get cart product details api response contains same products")
    public void getCartProductDetailsApiResponseContainsSameProducts() {
        //Sending getCartProducts api request
        getCartProductsAPIResponseWithCartProducts = given().spec(requestSpecification)
                .header("Authorization", loginAPIResponse.getToken())
                .pathParam("userId", loginAPIResponse.getUserId()).log().all().when()
                .get("/user/get-cart-products/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetCartProductsAPI_WithProductsSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetCartProductsAPI_WithCartproducts.class);
    }

    @Then("User should receive a list of product details get cart product details and {string} message")
    public void userShouldReceiveAListOfProductDetailsGetCartProductDetailsAndMessage(String message) {
        //Verifying count matches, message, products
//        Assert.assertEquals(count, getCartProductsAPIResponseWithCartProducts.getCount());
        Assert.assertEquals(message, getCartProductsAPIResponseWithCartProducts.getMessage());
        for (int i = 0; i < getCartProductsAPIResponseWithCartProducts.getCount(); i++) {
            Assert.assertTrue(cartProductIDs.contains(getCartProductsAPIResponseWithCartProducts.getProducts().get(i).get_id()));
        }

    }


}
