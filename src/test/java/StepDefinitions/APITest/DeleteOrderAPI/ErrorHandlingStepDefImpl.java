package StepDefinitions.APITest.DeleteOrderAPI;

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
    GetOrdersAPI_WithOrders getOrdersAPI;
    NoAccessTokenResponse noAccessTokenResponse;
    DeleteOrderAPI_SuccessResponse deleteOrderAPIResponse;
    List<Order> orders;
    Response response;

    @Given("The user has valid credentials delete order error handling")
    public void theUserHasValidCredentialsDeleteOrderErrorHandling() {
        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();
    }

    @When("The user sends a login API request delete order error handling")
    public void theUserSendsALoginAPIRequestDeleteOrderErrorHandling() {
        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token delete order error handling")
    public void theUserShouldReceiveAValidAuthenticationTokenDeleteOrderErrorHandling() {
        //Verifying token is not null
        Assert.assertNotNull(loginResponse.getToken());
    }

    @When("User sends get orders api request with valid userID delete order error handling")
    public void userSendsGetOrdersApiRequestWithValidUserIDDeleteOrderErrorHandling() {
        //Sending get orders api request
        getOrdersAPI = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .pathParam("userId", loginResponse.getUserId())
                .log().all().when()
                .get("order/get-orders-for-customer/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetOrdersAPI_WithOrders.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetOrdersAPI_WithOrders.class);
    }

    @Then("The user should receive order details get orders and {string} message delete order error handling")
    public void theUserShouldReceiveOrderDetailsGetOrdersAndMessageDeleteOrderErrorHandling(String message) {
        //Extracting orders
        orders = getOrdersAPI.getData();
        Assert.assertEquals(getOrdersAPI.getMessage(), message);
    }

    @When("User sends delete order api request with no order id")
    public void userSendsDeleteOrderApiRequestWithNoOrderId() {
        //Sending api request with no order id
        response = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .log().all()
                .when()
                .delete("order/delete-order/");
    }

    @Then("{string} error code is sent to user in delete order")
    public void errorCodeIsSentToUserInDeleteOrder(String code) {
        response.then()
                .spec(getResponseSpecification(Integer.parseInt(code), responseTime, ContentType.HTML)).log().all();
    }

    @When("User send request to delete order with wrong end point")
    public void userSendRequestToDeleteOrderWithWrongEndPoint() {
        //Sending api request with wrong end point
        response = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .pathParam("orderId", orders.get(0).get_id())
                .log().all()
                .when()
                .delete("order/delete/{orderId}");
    }

    @When("User send request to delete order with no auth token")
    public void userSendRequestToDeleteOrderWithNoAuthToken() {

        //Sending get orders api request with no auth token
        noAccessTokenResponse = given().spec(requestSpecification)
                .queryParam("id", orders.get(0).get_id())
                .when()
                .get("order/get-orders-details")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/NoAccessTokenSchema.json")))
                .spec(getResponseSpecification(401, responseTime, ContentType.JSON)).log().all()
                .extract().as(NoAccessTokenResponse.class);

    }

    @Then("{string} message is sent to user in delete order api response")
    public void messageIsSentToUserInDeleteOrderApiResponse(String message) {
        //Validating error message in the response
        Assert.assertEquals(noAccessTokenResponse.getMessage(), message);
    }

    @When("User send delte order request deleted order id")
    public void userSendDelteOrderRequestDeletedOrderId() {
        //Sending delete order request with valid order id
        deleteOrderAPIResponse = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .log().all().when()
                .delete("order/delete-order/677b5ec6e2b5443b1f14c342")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/DeleteOrderAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(400, responseTime, ContentType.JSON)).log().all()
                .extract().as(DeleteOrderAPI_SuccessResponse.class);
    }

    @Then("{string} error message is sent to user in delete order")
    public void errorMessageIsSentToUserInDeleteOrder(String message) {
        //Validating error message in the response
        Assert.assertEquals(deleteOrderAPIResponse.getMessage(), message);
    }
}
