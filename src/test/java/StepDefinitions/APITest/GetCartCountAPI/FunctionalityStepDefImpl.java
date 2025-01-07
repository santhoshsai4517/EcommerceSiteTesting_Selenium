package StepDefinitions.APITest.GetCartCountAPI;

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
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class FunctionalityStepDefImpl extends BaseTest {

    RequestSpecification request;
    LoginAPIResponse loginAPIResponse;
    GetCartCountAPI_NoCartProductsResponse cartResponse;
    GetCartCountAPI_CartProductsResponse cartProductsResponse;
    GetAllProductsAPIResponse getAllProductsAPIResponse;
    AddToCartAPI_SuccessResponse addToCartAPIResponse;
    List<Product> products;


    @Given("The user has valid credentials Get Cart Count")
    public void theUserHasValidCredentialsGetCartCount() {

        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();

    }

    @When("The user sends a login API request Get Cart Count")
    public void theUserSendsALoginAPIRequestGetCartCount() {

        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginAPIResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token Get Cart Count")
    public void theUserShouldReceiveAValidAuthenticationTokenGetCartCount() {
        //Verifying token is not null
        Assert.assertNotNull(loginAPIResponse.getToken());
    }


    @When("The user sends a request to get all products using the authentication token Get Cart Count")
    public void theUserSendsARequestToGetAllProductsUsingTheAuthenticationTokenGetCartCount() {
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

    @Then("The user should receive a list of product IDs Get Cart Count")
    public void theUserShouldReceiveAListOfProductIDsGetCartCount() {
        //Extracting product details
        products = getAllProductsAPIResponse.getData();
    }

    @When("User send request to get cart count with userId")
    public void userSendRequestToGetCartCountWithUserId() {

        //Sending get cart count api request and extracting response as GetCartCountAPI_NoCartProductsResponse object and validating response code and response time
        cartResponse = given().spec(requestSpecification)
                .header("Authorization", loginAPIResponse.getToken())
                .pathParam("userId", loginAPIResponse.getUserId())
                .when()
                .get("user/get-cart-count/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetCartCountAPI_NoCartFoundSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetCartCountAPI_NoCartProductsResponse.class);

    }


    @Then("User should receive {string} message")
    public void userShouldReceiveMessage(String message) {
        //Verifying message is as expected
        Assert.assertEquals(cartResponse.getMessage(), message);
    }


    @When("User adds {int} products to cart")
    public void userAddsProductsToCart(int count) {

        for (int i = 0; i < count; i++) {
            AddToCartAPIRequest requestBody = new AddToCartAPIRequest();
            requestBody.setProduct(products.get((int) (Math.random() * products.size())));
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

    @Then("User should receive {int} product count in response")
    public void userShouldReceiveProductCountInResponse(int count) {
        //Sending get cart count api request and extracting response as GetCartCountAPI_NoCartProductsResponse object and validating response code and response time
        cartProductsResponse = given().spec(requestSpecification)
                .header("Authorization", loginAPIResponse.getToken())
                .pathParam("userId", loginAPIResponse.getUserId())
                .when()
                .get("user/get-cart-count/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetCartCountAPI_CartFoundSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetCartCountAPI_CartProductsResponse.class);
    }
}
