package StepDefinitions.APITest.GetOrderDetailsAPI;

import BaseTest.BaseTest;
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
import java.util.List;

import static io.restassured.RestAssured.given;

public class ErrorHandlingStepDefImpl extends BaseTest {

    RequestSpecification request;
    LoginAPIResponse loginResponse;
    GetOrdersAPI_WithOrders getOrdersAPIWithOrders;
    GetOrderDetailsAPI_NoOrderFound noOrderFound;
    NoAccessTokenResponse noAccessTokenResponse;
    List<Order> orders;
    Response response;

    @Given("The user has valid credentials get order details error handling")
    public void theUserHasValidCredentialsGetOrderDetailsErrorHandling() {
        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();
    }

    @When("The user sends a login API request get order details error handling")
    public void theUserSendsALoginAPIRequestGetOrderDetailsErrorHandling() {
        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token get order details error handling")
    public void theUserShouldReceiveAValidAuthenticationTokenGetOrderDetailsErrorHandling() {
        //Verifying the user should receive a valid authentication token
        Assert.assertNotNull(loginResponse.getToken());
    }

    @When("The user sends a request to get all orders using the authentication token get order details error handling")
    public void theUserSendsARequestToGetAllOrdersUsingTheAuthenticationTokenGetOrderDetailsErrorHandling() {
        //Sending get orders api request
        getOrdersAPIWithOrders = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .pathParam("userId", loginResponse.getUserId())
                .log().all().when()
                .get("order/get-orders-for-customer/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetOrdersAPI_WithOrders.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetOrdersAPI_WithOrders.class);
    }

    @Then("The user should receive a list of orders get order details error handling")
    public void theUserShouldReceiveAListOfOrdersGetOrderDetailsErrorHandling() {
        //Extracting order details
        orders = getOrdersAPIWithOrders.getData();
    }

    @When("User sends get order details api request with no order id")
    public void userSendsGetOrderDetailsApiRequestWithNoOrderId() {
        //Sending api request with no order id
        noOrderFound = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .log().all()
                .when()
                .get("order/get-orders-details/")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetCartCountAPI_NoCartFoundSchema.json")))
                .spec(getResponseSpecification(400, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetOrderDetailsAPI_NoOrderFound.class);
    }


    @Then("{string} error message is sent to user in get order details")
    public void errorMessageIsSentToUserInGetOrderDetails(String message) {
        //Validating error code in the response
        Assert.assertEquals(noOrderFound.getMessage(), message);
    }

    @When("User send request to get order details with no auth token")
    public void userSendRequestToGetOrderDetailsWithNoAuthToken() {
        //Sending get orders api request with no auth token
        noAccessTokenResponse = given().spec(requestSpecification)
                .queryParam("id", orders.get(0).get_id())
                .when()
                .get("order/get-orders-details")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/NoAccessTokenSchema.json")))
                .spec(getResponseSpecification(401, responseTime, ContentType.JSON)).log().all()
                .extract().as(NoAccessTokenResponse.class);
    }

    @Then("{string} message is sent to user in get order details api response")
    public void messageIsSentToUserInGetOrderDetailsApiResponse(String message) {
        //Validating error message in the response
        Assert.assertEquals(noAccessTokenResponse.getMessage(), message);
    }

    @When("User send request to get order details with wrong end point")
    public void userSendRequestToGetOrderDetailsWithWrongEndPoint() {
        //Sending get orders api request with wrong end point
        response = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .queryParam("id", orders.get(0).get_id())
                .log().all()
                .when()
                .get("order/get-order");
    }

    @Then("User should receive {string} error code get order details error handling")
    public void userShouldReceiveErrorCodeGetOrderDetailsErrorHandling(String code) {
        response
                .then()
                .spec(getResponseSpecification(Integer.parseInt(code), responseTime, ContentType.HTML)).log().all();
    }

}
