package StepDefinitions.APITest.GetCartProductsAPI;

import BaseTest.BaseTest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.GetCartProductsAPI_NoCartProducts;
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
    LoginAPIResponse loginAPIResponse;
    NoAccessTokenResponse noAccessTokenResponse;
    GetCartProductsAPI_NoCartProducts getCartProductsAPINoCartProducts;
    Response response;

    @Given("user has valid credentials get cart product details")
    public void userHasValidCredentialsGetCartProductDetails() {
        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();
    }

    @When("user sends a login API request get cart product details")
    public void userSendsALoginAPIRequestGetCartProductDetails() {
        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginAPIResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);
    }

    @Then("user should receive a valid authentication token get cart product details")
    public void userShouldReceiveAValidAuthenticationTokenGetCartProductDetails() {
        //Verifying token is not null
        Assert.assertNotNull(loginAPIResponse.getToken());
    }

    @When("User does not send auth token in get cart products api request")
    public void userDoesNotSendAuthTokenInGetCartProduictsApiRequest() {
        //Sending get cart count api request and extracting response as GetCartCountAPI_NoCartProductsResponse object and validating response code and response time
        noAccessTokenResponse = given().spec(requestSpecification)
                .pathParam("userId", loginAPIResponse.getUserId()).log().all()
                .when()
                .get("user/get-cart-products/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/NoAccessTokenSchema.json")))
                .spec(getResponseSpecification(401, responseTime, ContentType.JSON)).log().all()
                .extract().as(NoAccessTokenResponse.class);
    }

    @Then("{string} message is returned in get cart products api response to user")
    public void messageIsReturnedInGetCartProductsApiResponseToUser(String message) {
        //Validating error message in the response
        Assert.assertEquals(noAccessTokenResponse.getMessage(), message);
    }

    @When("User sends get cart products to wrong endpoint")
    public void userSendsGetCartProductsToWrongEndpoint() {
        //Sending get cart count api request with wrong end point
        response = given().spec(requestSpecification)
                .header("Authorization", loginAPIResponse.getToken())
                .pathParam("userID", loginAPIResponse.getUserId()).when()
                .get("user/get-cart/{userID}");
    }

    @Then("{string} code is returned in get cart products api response to user")
    public void codeIsReturnedInGetCartProductsApiResponseToUser(String code) {
        //Validating error code in the response
        response
                .then()
                .spec(getResponseSpecification(Integer.parseInt(code), responseTime, ContentType.HTML)).log().all();
    }

    @When("User send request to get cart products with no userId")
    public void userSendRequestToGetCartProductsWithNoUserId() {
        response = given().spec(requestSpecification)
                .header("Authorization", loginAPIResponse.getToken()).when()
                .get("user/get-cart-products/");
    }

    @When("User send request to get cart products with wrong userId")
    public void userSendRequestToGetCartProductsWithWrongUserId() {
        getCartProductsAPINoCartProducts = given().spec(requestSpecification)
                .header("Authorization", loginAPIResponse.getToken())
                .pathParam("userId", "123sa")
                .when()
                .get("user/get-cart-products/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetCartProductsAPI_NoProductsSchema.json")))
                .spec(getResponseSpecification(400, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetCartProductsAPI_NoCartProducts.class);
    }

    @Then("{string} error message is sent to user in get cart products api response")
    public void errorMessageIsSentToUserInGetCartProductsApiResponse(String message) {
        //Validating error message in the response
        Assert.assertEquals(getCartProductsAPINoCartProducts.getMessage(), message);
    }
}
