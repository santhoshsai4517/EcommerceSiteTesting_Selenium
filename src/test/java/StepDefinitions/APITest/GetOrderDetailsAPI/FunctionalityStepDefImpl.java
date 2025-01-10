package StepDefinitions.APITest.GetOrderDetailsAPI;

import BaseTest.BaseTest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.GetOrderDetailsAPIResponse;
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
import java.util.Random;

import static io.restassured.RestAssured.given;

public class FunctionalityStepDefImpl extends BaseTest {

    RequestSpecification request;
    LoginAPIResponse loginResponse;
    GetOrdersAPI_WithOrders getOrdersAPIWithOrders;
    GetOrderDetailsAPIResponse getOrderDetailsAPIResponse;
    List<Order> orders;
    Order order;


    @Given("The user has valid credentials get order details functionality")
    public void theUserHasValidCredentialsGetOrderDetailsFunctionality() {

        //Creating login api request
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();
    }

    @When("The user sends a login API request get order details functionality")
    public void theUserSendsALoginAPIRequestGetOrderDetailsFunctionality() {
        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token get order details functionality")
    public void theUserShouldReceiveAValidAuthenticationTokenGetOrderDetailsFunctionality() {
        //Verifying token is not null
        Assert.assertNotNull(loginResponse.getToken());
    }

    @When("The user sends a request to get all orders using the authentication token get order details functionality")
    public void theUserSendsARequestToGetAllOrdersUsingTheAuthenticationTokenGetOrederDetailsFunctionality() {
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

    @Then("The user should receive a list of orders get order details functionality")
    public void theUserShouldReceiveAListOfOrdersGetOrderDetailsFunctionality() {
        //Extracting order details
        orders = getOrdersAPIWithOrders.getData();
    }

    @When("The user sends a request to get order details for all orders")
    public void theUserSendsARequestToGetOrderDetailsForAllOrders() {

        Random rand = new Random();
        order = orders.get(rand.nextInt(orders.size()));

        //Creating request body for get orders api request
        getOrderDetailsAPIResponse = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .queryParam("id", order.get_id())
                .log().all().when()
                .get("order/get-orders-details")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetOrderDetailsAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetOrderDetailsAPIResponse.class);
    }

    @Then("{string} message is returned and same order is returned")
    public void messageIsReturnedAndSameOrderIsReturned(String message) {

        //Validating order details
        Assert.assertEquals(getOrderDetailsAPIResponse.getMessage(), message);
        Assert.assertEquals(getOrderDetailsAPIResponse.getData().get_id(), order.get_id());
        Assert.assertEquals(getOrderDetailsAPIResponse.getData().getOrderById(), loginResponse.getUserId());
    }

}
