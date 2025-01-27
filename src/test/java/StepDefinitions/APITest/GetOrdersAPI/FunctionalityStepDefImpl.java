package StepDefinitions.APITest.GetOrdersAPI;

import BaseTest.BaseTest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.GetOrdersAPI_NoOrders;
import POJO.Response.GetOrdersAPI_WithOrders;
import POJO.Response.LoginAPIResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;

import static io.restassured.RestAssured.given;

public class FunctionalityStepDefImpl extends BaseTest {

    RequestSpecification request;
    LoginAPIResponse loginResponse;
    GetOrdersAPI_WithOrders getOrdersAPIWithOrders;
    GetOrdersAPI_NoOrders getOrdersAPINoOrders;

    @Given("The user has valid credentials get orders")
    public void theUserHasValidCredentialsGetOrders() {

        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();
    }

    @When("The user sends a login API request get orders")
    public void theUserSendsALoginAPIRequestGetOrders() {
        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token get orders")
    public void theUserShouldReceiveAValidAuthenticationTokenGetOrders() {
        //Verifying the user should receive a valid authentication token
        Assert.assertNotNull(loginResponse.getToken());
    }

    @When("User sends get orders api request with valid userID")
    public void userSendsGetOrdersApiRequestWithValidUserID() {
        //Creating request body for get orders api request
        getOrdersAPIWithOrders = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .pathParam("userId", loginResponse.getUserId())
                .log().all().when()
                .get("order/get-orders-for-customer/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetOrdersAPI_WithOrders.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetOrdersAPI_WithOrders.class);
    }


    @Then("The user should receive order details get orders and {string} message")
    public void theUserShouldReceiveOrderDetailsGetOrdersAndMessage(String message) {
        //Verifying order details and message
        Assert.assertEquals(getOrdersAPIWithOrders.getMessage(), message);
        Assert.assertTrue(getOrdersAPIWithOrders.getCount() > 0);
    }


    @When("User with no orders sends get orders api request with valid userID")
    public void userWithNoOrdersSendsGetOrdersApiRequestWithValidUserID() {
        //Creating request body for login api request
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("s1234@gmail.com");
        requestBody.setUserPassword("123");

        //Sending login api request
        loginResponse = given().spec(requestSpecification).body(requestBody)
                .log().all().when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);

        //Creating request body for get orders api request
        getOrdersAPINoOrders = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .pathParam("userId", loginResponse.getUserId())
                .log().all().when()
                .get("order/get-orders-for-customer/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetOrdersAPI_NoOrders.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetOrdersAPI_NoOrders.class);


    }

    @Then("The user should receive no order details get orders and {string} message")
    public void theUserShouldReceiveNoOrderDetailsGetOrdersAndMessage(String message) {
        //Verifying no order details and message
        Assert.assertEquals(getOrdersAPINoOrders.getMessage(), message);
        Assert.assertEquals(getOrdersAPINoOrders.getData().size(), 0);
    }

}
