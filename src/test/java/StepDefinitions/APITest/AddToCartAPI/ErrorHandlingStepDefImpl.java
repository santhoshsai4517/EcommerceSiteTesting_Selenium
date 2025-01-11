package StepDefinitions.APITest.AddToCartAPI;

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
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ErrorHandlingStepDefImpl extends BaseTest {

    RequestSpecification request;
    LoginAPIResponse loginResponse;
    GetAllProductsAPIResponse getAllProductsAPIResponse;
    AddToCartAPI_SuccessResponse addToCartAPIResponse;
    NoAccessTokenResponse noAccessTokenResponse;

    Response response;
    List<Product> products;

    @Given("The user has valid credentials add to cart error handling")
    public void theUserHasValidCredentialsAddToCartErrorHandling() {

        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();

    }

    @When("The user sends a login API request add to cart error handling")
    public void theUserSendsALoginAPIRequestAddToCartErrorHandling() {

        //Sending login api request body
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);

    }

    @Then("The user should receive a valid authentication token add to cart error handling")
    public void theUserShouldReceiveAValidAuthenticationTokenAddToCartErrorHandling() {
        //Verifying the user should receive a valid authentication token
        Assert.assertNotNull(loginResponse.getToken());
    }

    @When("The user sends a request to get all products using the authentication token add to cart error handling")
    public void theUserSendsARequestToGetAllProductsUsingTheAuthenticationTokenAddToCartErrorHandling() {

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
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .post("/product/get-all-products")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetAllProductsAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(GetAllProductsAPIResponse.class);

    }

    @Then("The user should receive a list of product IDs add to cart error handling")
    public void theUserShouldReceiveAListOfProductIDsAddToCartErrorHandling() {
        //Extracting product details
        products = getAllProductsAPIResponse.getData();
    }

    @When("User send request to add to cart with no userId")
    public void userSendRequestToAddToCartWithNoUserId() {
        //Creating request body for add to cart api request with no userId
        AddToCartAPIRequest requestBody = new AddToCartAPIRequest();
        requestBody.setProduct(products.get(0));

        //Sending add to cart api request body for add to cart api request
        response = given().spec(requestSpecification).body(requestBody)
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .post("/user/add-to-cart");
    }

    @Then("User should receive {string} error code and {string} error message")
    public void userShouldReceiveErrorCodeAndErrorMessage(String code, String message) {
        //Verifying the user should receive the expected error code and message
        addToCartAPIResponse = response.then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/AddToCartAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(Integer.parseInt(code), responseTime, ContentType.JSON)).log().all()
                .extract().as(AddToCartAPI_SuccessResponse.class);
        Assert.assertEquals(message, addToCartAPIResponse.getMessage());
    }

    @When("User send request to add to cart with wrong end point")
    public void userSendRequestToAddToCartWithWrongEndPoint() {
        response = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken()).when()
                .get("/user/add");
    }

    @Then("User should receive {string} error code in Add to cart response")
    public void userShouldReceiveErrorCodeInAddToCartResponse(String code) {
        response.then()
                .spec(getResponseSpecification(Integer.parseInt(code), responseTime, ContentType.HTML)).log().all();
    }

    @When("User send request to add to cart with no auth token")
    public void userSendRequestToAddToCartWithNoAuthToken() {

        AddToCartAPIRequest requestBody = new AddToCartAPIRequest();
        requestBody.setProduct(products.get(0));

        // Sending request with no access token
        noAccessTokenResponse = given().spec(requestSpecification).body(requestBody)
                .log().all()
                .when()
                .post("/user/add-to-cart")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/NoAccessTokenSchema.json")))
                .spec(getResponseSpecification(401, responseTime, ContentType.JSON)).log().all()
                .extract().as(NoAccessTokenResponse.class);
    }

    @Then("{string} message is sent to user in add to cart response")
    public void messageIsSentToUserInAddToCartResponse(String message) {
        Assert.assertEquals(message, noAccessTokenResponse.getMessage());
    }

    @When("User send request to add to cart with wrong userId")
    public void userSendRequestToAddToCartWithWrongUserId() {

        AddToCartAPIRequest requestBody = new AddToCartAPIRequest();
        requestBody.setProduct(products.get(0));
        requestBody.set_id("123sdf");

        response = given().spec(requestSpecification).body(requestBody)
                .header("Authorization", loginResponse.getToken()).log().all()
                .when()
                .post("/user/add-to-cart");
    }

}
