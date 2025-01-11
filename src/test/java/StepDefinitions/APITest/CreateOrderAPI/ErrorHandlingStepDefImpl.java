package StepDefinitions.APITest.CreateOrderAPI;

import BaseTest.BaseTest;
import POJO.Request.CreateOrderAPIRequest;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ErrorHandlingStepDefImpl extends BaseTest {

    RequestSpecification request;
    LoginAPIResponse loginResponse;
    GetAllProductsAPIResponse getAllProductsAPIResponse;
    NoAccessTokenResponse noAccessTokenResponse;
    CreateOrderAPI_NoBodyResponse createOrderAPI_NoBodyResponse;
    List<Product> products;
    Response response;

    @Given("The user has valid credentials create order error handling")
    public void theUserHasValidCredentialsCreateOrderErrorHandling() {
        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();
    }

    @When("The user sends a login API request create order error handling")
    public void theUserSendsALoginAPIRequestCreateOrderErrorHandling() {
        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token create order error handling")
    public void theUserShouldReceiveAValidAuthenticationTokenCreateOrderErrorHandling() {
        //Verifying the user should receive a valid authentication token
        Assert.assertNotNull(loginResponse.getToken());
    }

    @When("The user sends a request to get all products using the authentication token create order error handling")
    public void theUserSendsARequestToGetAllProductsUsingTheAuthenticationTokenCreateOrderErrorHandling() {
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

    @Then("The user should receive a list of product IDs create order error handling")
    public void theUserShouldReceiveAListOfProductIDsCreateOrderErrorHandling() {
        //Extracting product details
        products = getAllProductsAPIResponse.getData();
    }

    @When("User send request to create order with wrong end point")
    public void userSendRequestToCreateOrderWithWrongEndPoint() {
        //Creating request body for create order api request
        POJO.Request.Order order1 = new POJO.Request.Order("India", "676a631fe2b5443b1f004a20");
        POJO.Request.Order order2 = new POJO.Request.Order("India", "676a9421e2b5443b1f00a1b2");

        // Create OrdersRequest with a list of orders
        CreateOrderAPIRequest ordersRequest = new CreateOrderAPIRequest(Arrays.asList(order1, order2));
        response = given().spec(requestSpecification).body(ordersRequest)
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .post("order/order");
    }

    @Then("User should receive {string} error code in create order response")
    public void userShouldReceiveErrorCodeInCreateOrderResponse(String code) {
        response.then()
                .spec(getResponseSpecification(Integer.parseInt(code), responseTime, ContentType.HTML)).log().all();
    }


    @When("User send create order with no auth token")
    public void userSendCreateOrderWithNoAuthToken() {

        //Creating request body for create order api request
        POJO.Request.Order order1 = new POJO.Request.Order("India", "676a631fe2b5443b1f004a20");
        POJO.Request.Order order2 = new POJO.Request.Order("India", "676a9421e2b5443b1f00a1b2");

        // Create OrdersRequest with a list of orders
        CreateOrderAPIRequest ordersRequest = new CreateOrderAPIRequest(Arrays.asList(order1, order2));
        // Sending request with no access token
        noAccessTokenResponse = given().spec(requestSpecification).body(ordersRequest)
                .log().all()
                .when()
                .post("order/create-order")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/NoAccessTokenSchema.json")))
                .spec(getResponseSpecification(401, responseTime, ContentType.JSON)).log().all()
                .extract().as(NoAccessTokenResponse.class);
    }

    @Then("{string} message is sent to user in create order response")
    public void messageIsSentToUserInCreateOrderResponse(String message) {
        Assert.assertEquals(message, noAccessTokenResponse.getMessage());

    }

    @When("User send request to create order with no body")
    public void userSendRequestToCreateOrderWithNoBody() {
        //Sending request to create order with nobody
        createOrderAPI_NoBodyResponse = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .post("order/create-order")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/CreateOrderAPI_NoBodySchema.json")))
                .spec(getResponseSpecification(500, responseTime, ContentType.JSON)).log().all()
                .extract().as(CreateOrderAPI_NoBodyResponse.class);
    }

    @Then("{string} message is snet to user in create order response")
    public void messageIsSnetToUserInCreateOrderResponse(String message) {
        //Validating the message
        Assert.assertEquals(message, createOrderAPI_NoBodyResponse.getType());
    }
}
