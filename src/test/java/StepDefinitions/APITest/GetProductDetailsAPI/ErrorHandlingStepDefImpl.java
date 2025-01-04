package StepDefinitions.APITest.GetProductDetailsAPI;

import BaseTest.BaseTest;
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
    GetProductDetailsAPI_NoProductResponse getProductDetailsResponse;
    NoAccessTokenResponse noAccessTokenResponse;
    List<Product> products;
    Response response;

    @Given("user has valid credentials")
    public void userHasValidCredentials() {
        LoginAPIRequest loginRequest = new LoginAPIRequest();
        loginRequest.setUserEmail("santhoshsai4517@gmail.com");
        loginRequest.setUserPassword("151Fa04124@4517");

        request = given().spec(requestSpecification).body(loginRequest).log().all();
    }

    @When("user sends a login API request")
    public void userSendsALoginAPIRequest() {
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, 2000, ContentType.JSON)).log().all()
                .extract()
                .as(LoginAPIResponse.class);
    }

    @Then("user should receive a valid authentication token")
    public void userShouldReceiveAValidAuthenticationToken() {
        Assert.assertNotNull(loginResponse.getToken());
    }

    @When("user sends a request to get all products using the authentication token")
    public void userSendsARequestToGetAllProductsUsingTheAuthenticationToken() {
        GetAllProductsAPIRequest requestBody = new GetAllProductsAPIRequest();
        requestBody.setProductName("");
        requestBody.setMinPrice(null);
        requestBody.setMaxPrice(null);
        requestBody.setProductCategory(Collections.emptyList());
        requestBody.setProductSubCategory(Collections.emptyList());
        requestBody.setProductFor(Collections.emptyList());

        getAllProductsAPIResponse = given().spec(requestSpecification).body(requestBody)
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .post("/product/get-all-products")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetAllProductsAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, 2000, ContentType.JSON)).log().all()
                .extract()
                .as(GetAllProductsAPIResponse.class);
    }

    @Then("user should receive a list of product IDs")
    public void userShouldReceiveAListOfProductIDs() {
        products = getAllProductsAPIResponse.getData();
    }

    @When("User does not send auth token in api request")
    public void userDoesNotSendAuthTokenInApiRequest() {
        noAccessTokenResponse = given().spec(requestSpecification).pathParam("productId", products.get(0).get_id())
                .header("Authorization", "").log().all().when()
                .get("/product/get-product-detail/{productId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/NoAccessTokenSchema.json")))
                .spec(getResponseSpecification(401, 2000, ContentType.JSON)).log().all()
                .extract()
                .as(NoAccessTokenResponse.class);
    }

    @Then("{string} message is returned in response to user")
    public void messageIsReturnedInResponseToUser(String message) {
        Assert.assertEquals(message, noAccessTokenResponse.getMessage());
    }

    @When("User does not send productID in api request")
    public void userDoesNotSendProductIDInApiRequest() {
        response = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .get("/product/get-product-detail/");
    }

    @Then("{string} code is returned in response to user")
    public void codeIsReturnedInResponseToUser(String errorCode) {
        response.then().spec(getResponseSpecification(Integer.parseInt(errorCode), 2000, ContentType.HTML)).log().all();
    }

    @When("User sends product id that does not exists")
    public void userSendsProductIdThatDoesNotExists() {
        getProductDetailsResponse = given().spec(requestSpecification).pathParam("productId", "6581cade9fd99c85e8ee7ff2")
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .get("/product/get-product-detail/{productId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetProductDetails_NoProductSchema.json")))
                .spec(getResponseSpecification(400, 2000, ContentType.JSON)).log().all()
                .extract()
                .as(GetProductDetailsAPI_NoProductResponse.class);
    }

    @Then("{string} error message is returned in response to user")
    public void errorMessageIsReturnedInResponseToUser(String message) {
        Assert.assertEquals(message, getProductDetailsResponse.getMessage());
    }

    @When("User sends product id to wrong endpoint")
    public void userSendsProductIdToWrongEndpoint() {
        response = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken()).pathParam("productId", products.get(0).get_id())
                .log().all().when()
                .get("/product/get-product/{productId}");

    }
}
