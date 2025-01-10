package StepDefinitions.APITest.DeleteOrderAPI;

import BaseTest.BaseTest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.DeleteOrderAPI_SuccessResponse;
import POJO.Response.GetOrdersAPI_WithOrders;
import POJO.Response.LoginAPIResponse;
import POJO.Response.Order;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.given;

public class FunctionalityStepDefImpl extends BaseTest {

    RequestSpecification request;
    LoginAPIResponse loginResponse;
    DeleteOrderAPI_SuccessResponse deleteOrderAPIResponse;
    GetOrdersAPI_WithOrders getOrdersAPI;
    List<Order> orders;

    @Given("The user has valid credentials delete order")
    public void theUserHasValidCredentialsDeleteOrder() {
        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();
    }

    @When("The user sends a login API request delete order")
    public void theUserSendsALoginAPIRequestDeleteOrder() {
        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token delete order")
    public void theUserShouldReceiveAValidAuthenticationTokenDeleteOrder() {
        //Verifying token is not null
        Assert.assertNotNull(loginResponse.getToken());
    }

    @When("User sends get orders api request with valid userID delete order")
    public void userSendsGetOrdersApiRequestWithValidUserIDDeleteOrder() {
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

    @Then("The user should receive order details get orders and {string} message delete order")
    public void theUserShouldReceiveOrderDetailsGetOrdersAndMessageDeleteOrder(String message) {
        //Extracting orders
        orders = getOrdersAPI.getData();
        Assert.assertEquals(getOrdersAPI.getMessage(), message);
    }

    @When("User sends valid order id to delete")
    public void userSendsValidOrderIdToDelete() {
        //Sending delete order request with valid order id
        deleteOrderAPIResponse = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .pathParam("orderId", orders.get(0).get_id())
                .log().all().when()
                .delete("order/delete-order/{orderId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/DeleteOrderAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(DeleteOrderAPI_SuccessResponse.class);
    }

    @Then("User receives {string} message in delete order api response")
    public void userReceivesMessageInDeleteOrderApiResponse(String message) {
        //Verifying message in response
        Assert.assertEquals(deleteOrderAPIResponse.getMessage(), message);
    }

}
