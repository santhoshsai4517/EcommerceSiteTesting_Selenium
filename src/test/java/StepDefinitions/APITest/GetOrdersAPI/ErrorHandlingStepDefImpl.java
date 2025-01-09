package StepDefinitions.APITest.GetOrdersAPI;

import BaseTest.BaseTest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.GetOrdersAPI_NoOrders;
import POJO.Response.LoginAPIResponse;
import POJO.Response.NoAccessTokenResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;

import static io.restassured.RestAssured.given;

public class ErrorHandlingStepDefImpl extends BaseTest {

    RequestSpecification request;
    LoginAPIResponse loginResponse;
    NoAccessTokenResponse noAccessTokenResponse;
    GetOrdersAPI_NoOrders getOrdersAPINoOrders;
    Response response;

    @Given("The user has valid credentials get orders error handling")
    public void theUserHasValidCredentialsGetOrdersErrorHandling() {

        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();
    }

    @When("The user sends a login API request get orders error handling")
    public void theUserSendsALoginAPIRequestGetOrdersErrorHandling() {

        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token get orders error handling")
    public void theUserShouldReceiveAValidAuthenticationTokenGetOrdersErrorHandling() {
        //Verifying token is not null
        Assert.assertNotNull(loginResponse.getToken());
    }

    @When("User sends get orders api request with no userID")
    public void userSendsGetOrdersApiRequestWithNoUserID() {
        //Creating request body for get orders api request with no user id
        response = given().spec(requestSpecification).log().all()
                .header("Authorization", loginResponse.getToken()).when()
                .get("order/get-orders-for-customer/");
    }

    @Then("User should receive {string} error code get orders error handling")
    public void userShouldReceiveErrorCodeGetOrdersErrorHandling(String code) {
        //Validating error code in the response
        response
                .then()
                .spec(getResponseSpecification(Integer.parseInt(code), responseTime, ContentType.HTML)).log().all();
    }

    @When("User send request to get orders with wrong end point")
    public void userSendRequestToGetOrdersWithWrongEndPoint() {
        //Sending get orders api request with wrong end point
        response = given().spec(requestSpecification).log().all()
                .header("Authorization", loginResponse.getToken())
                .pathParam("userId", loginResponse.getUserId()).when()
                .get("order/get-orders-for/{userId}");
    }

    @When("User send request to get orders with no auth token")
    public void userSendRequestToGetOrdersWithNoAuthToken() {
        //Sending get orders api request with no auth token
        noAccessTokenResponse = given().spec(requestSpecification)
                .pathParam("userId", loginResponse.getUserId())
                .when()
                .get("order/get-orders-for-customer/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/NoAccessTokenSchema.json")))
                .spec(getResponseSpecification(401, responseTime, ContentType.JSON)).log().all()
                .extract().as(NoAccessTokenResponse.class);
    }

    @Then("{string} message is sent to user in get orders api response")
    public void messageIsSentToUserInGetOrdersApiResponse(String message) {
        //Validating error message in the response
        Assert.assertEquals(noAccessTokenResponse.getMessage(), message);
    }

    @When("User send request to get orders api with wrong userId")
    public void userSendRequestToGetOrdersApiWithWrongUserId() {
        //Sending api request with wrong user id
        getOrdersAPINoOrders = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .pathParam("userId", "123sa").log().all()
                .when()
                .get("user/get-cart-count/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetCartCountAPI_NoCartFoundSchema.json")))
                .spec(getResponseSpecification(400, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetOrdersAPI_NoOrders.class);
    }

    @Then("{string} error message is sent to user in get orders")
    public void errorMessageIsSentToUserInGetOrders(String message) {
        //Validating error message in the response
        Assert.assertEquals(getOrdersAPINoOrders.getMessage(), message);
    }
}
