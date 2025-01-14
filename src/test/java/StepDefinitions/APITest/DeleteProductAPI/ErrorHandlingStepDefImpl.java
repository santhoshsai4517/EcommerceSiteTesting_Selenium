package StepDefinitions.APITest.DeleteProductAPI;

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
    NoAccessTokenResponse noAccessTokenResponse;
    DeleteProductAPI_SuccessResponse deleteProductAPI_SuccessResponse;
    Response response;
    List<Product> products;


    @Given("The user has valid credentials delete product error handling")
    public void theUserHasValidCredentialsDeleteProductErrorHandling() {
        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();
    }

    @When("The user sends a login API request delete product error handling")
    public void theUserSendsALoginAPIRequestDeleteProductErrorHandling() {
        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token delete product error handling")
    public void theUserShouldReceiveAValidAuthenticationTokenDeleteProductErrorHandling() {
        //Verifying the user should receive a valid authentication token
        Assert.assertNotNull(loginResponse.getToken());
    }

    @When("User sends get products api request with valid userID delete product error handling")
    public void userSendsGetProductsApiRequestWithValidUserIDDeleteProductErrorHandling() {
        //Creating get products api body
        GetAllProductsAPIRequest requestBody = new GetAllProductsAPIRequest();
        requestBody.setProductName("");
        requestBody.setMinPrice(null);
        requestBody.setMaxPrice(null);
        requestBody.setProductCategory(Collections.emptyList());
        requestBody.setProductSubCategory(Collections.emptyList());
        requestBody.setProductFor(Collections.emptyList());

        //Sending get products api request
        getAllProductsAPIResponse = given().spec(requestSpecification).body(requestBody)
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .post("/product/get-all-products")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetAllProductsAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(GetAllProductsAPIResponse.class);
    }

    @Then("The user should receive order details get products delete product error handling")
    public void theUserShouldReceiveOrderDetailsGetProductsDeleteProductErrorHandling() {
        //Extracting product details
        products = getAllProductsAPIResponse.getData();
    }

    @When("User send request to delete product with wrong end point")
    public void userSendRequestToDeleteProductWithWrongEndPoint() {
        response = given().spec(requestSpecification).header("Authorization", loginResponse.getToken())
                .pathParam("productId", products.get(0).get_id()).log().all().when()
                .delete("product/delete/{productId}");
    }

    @Then("{string} error code is sent to user in delete product")
    public void errorCodeIsSentToUserInDeleteProduct(String code) {
        response.then().spec(getResponseSpecification(Integer.parseInt(code), responseTime, ContentType.HTML)).log().all();
    }

    @When("User send request to delete product with no auth token")
    public void userSendRequestToDeleteProductWithNoAuthToken() {
        noAccessTokenResponse = given().spec(requestSpecification)
                .pathParam("productId", products.get(0).get_id()).log().all().when()
                .delete("product/delete-product/{productId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/NoAccessTokenSchema.json")))
                .spec(getResponseSpecification(401, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(NoAccessTokenResponse.class);
    }

    @Then("{string} message is sent to user in delete product api response")
    public void messageIsSentToUserInDeleteProductApiResponse(String message) {
        Assert.assertEquals(noAccessTokenResponse.getMessage(), message);
    }


    @When("User sends delete product api request with no order id")
    public void userSendsDeleteProductApiRequestWithNoOrderId() {
        response = given().spec(requestSpecification).header("Authorization", loginResponse.getToken())
                .log().all().when()
                .delete("product/delete");
    }

    @When("User send delete product request with deleted product id")
    public void userSendDeleteProductRequestWithDeletedProductId() {
        deleteProductAPI_SuccessResponse = given().spec(requestSpecification).header("Authorization", loginResponse.getToken())
                .pathParam("productId", "6782124ae2b5443b1f1e135e").log().all().when()
                .delete("product/delete-product/{productId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/DeleteProductAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(400, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(DeleteProductAPI_SuccessResponse.class);
    }

    @Then("{string} error message is sent to user in delete product")
    public void errorMessageIsSentToUserInDeleteProduct(String message) {
        Assert.assertEquals(deleteProductAPI_SuccessResponse.getMessage(), message);
    }

}
