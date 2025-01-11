package StepDefinitions.APITest.CreateOrderAPI;

import BaseTest.BaseTest;
import POJO.Request.CreateOrderAPIRequest;
import POJO.Request.GetAllProductsAPIRequest;
import POJO.Request.LoginAPIRequest;
import POJO.Request.Order;
import POJO.Response.CreateOrderAPI_SuccessResponse;
import POJO.Response.GetAllProductsAPIResponse;
import POJO.Response.LoginAPIResponse;
import POJO.Response.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class FunctionalityStepDefImpl extends BaseTest {

    RequestSpecification request;
    LoginAPIResponse loginAPIResponse;
    GetAllProductsAPIResponse getAllProductsAPIResponse;
    CreateOrderAPI_SuccessResponse createOrderAPIResponse;
    List<Product> products;
    List<Order> orders = new ArrayList<>();


    @Given("The user has valid credentials create order")
    public void theUserHasValidCredentialsCreateOrder() {
        //Creating login api request body
        LoginAPIRequest loginAPIRequest = new LoginAPIRequest();
        loginAPIRequest.setUserEmail("santhoshsai4517@gmail.com");
        loginAPIRequest.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(loginAPIRequest).log().all();
    }

    @When("The user sends a login API request create order")
    public void theUserSendsALoginAPIRequestCreateOrder() {
        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginAPIResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token create order")
    public void theUserShouldReceiveAValidAuthenticationTokenCreateOrder() {
        //Validating token is not null
        Assert.assertNotNull(loginAPIResponse.getToken());
    }

    @When("The user sends a request to get all products using the authentication token create order")
    public void theUserSendsARequestToGetAllProductsUsingTheAuthenticationTokenCreateOrder() {
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
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(GetAllProductsAPIResponse.class);
    }

    @Then("The user should receive a list of product IDs create order")
    public void theUserShouldReceiveAListOfProductIDsCreateOrder() {
        //Extracting product details
        products = getAllProductsAPIResponse.getData();
    }

    @When("User sends create order api request with product details")
    public void userSendsCreateOrderApiRequestWithProductDetails() throws JsonProcessingException {
        //Creating request body for create order api request
        Order order1 = new Order("India", "676a631fe2b5443b1f004a20");
        Order order2 = new Order("India", "676a9421e2b5443b1f00a1b2");

        // Create OrdersRequest with a list of orders
        CreateOrderAPIRequest ordersRequest = new CreateOrderAPIRequest(Arrays.asList(order1, order2));
//        System.out.println(orders);
        createOrderAPIResponse = given().spec(requestSpecification).body(ordersRequest)
                .header("Authorization", loginAPIResponse.getToken()).log().all().when()
                .post("order/create-order")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/CreateOrderAPI_SuccessShema.json")))
                .spec(getResponseSpecification(201, responseTime, ContentType.JSON)).log().all()
                .extract().as(CreateOrderAPI_SuccessResponse.class);
    }

    @Then("{string} message is sent along with order ids in create order api response")
    public void messageIsSentAlongWithOrderIdsInCreateOrderApiResponse(String message) {
        //Verifying message in response
        Assert.assertEquals(createOrderAPIResponse.getMessage(), message);
    }
}
