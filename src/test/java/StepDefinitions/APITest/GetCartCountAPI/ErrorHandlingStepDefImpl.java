package StepDefinitions.APITest.GetCartCountAPI;

import BaseTest.BaseTest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.GetCartCountAPI_NoCartProductsResponse;
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
    GetCartCountAPI_NoCartProductsResponse cartResponse;
    Response response;
    NoAccessTokenResponse noAccessTokenResponse;

    @Given("The user has valid credentials Get Cart Count error handling")
    public void theUserHasValidCredentialsGetCartCountErrorHandling() {
        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();

    }

    @When("The user sends a login API request Get Cart Count error handling")
    public void theUserSendsALoginAPIRequestGetCartCountErrorHandling() {

        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginAPIResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token Get Cart Count error handling")
    public void theUserShouldReceiveAValidAuthenticationTokenGetCartCountErrorHandling() {
        //Verifying token is not null
        Assert.assertNotNull(loginAPIResponse.getToken());
    }

    @When("User send request to get cart count with no userId")
    public void userSendRequestToGetCartCountWithNoUserId() {

        //Sending get cart count api request with no userId
        response = given().spec(requestSpecification)
                .header("Authorization", loginAPIResponse.getToken()).when()
                .get("user/get-cart-count/");


    }


    @Then("User should receive {string} error code")
    public void userShouldReceiveErrorCode(String code) {

        //Validating error code in the response
        response
                .then()
                .spec(getResponseSpecification(Integer.parseInt(code), responseTime, ContentType.HTML)).log().all();
    }

    @When("User send request to get cart count with wrong end point")
    public void userSendRequestToGetCartCountWithWrongEndPoint() {

        //Sending get cart count api request with wrong end point
        response = given().spec(requestSpecification)
                .header("Authorization", loginAPIResponse.getToken()).when()
                .get("user/get-cart");
    }

    @When("User send request to get cart count with no auth token")
    public void userSendRequestToGetCartCountWithNoAuthToken() {

        //Sending get cart count api request with no auth token
        noAccessTokenResponse = given().spec(requestSpecification)
                .pathParam("userId", loginAPIResponse.getUserId())
                .when()
                .get("user/get-cart-count/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/NoAccessTokenSchema.json")))
                .spec(getResponseSpecification(401, responseTime, ContentType.JSON)).log().all()
                .extract().as(NoAccessTokenResponse.class);

    }

    @Then("{string} message is sent to user")
    public void messageIsSentToUser(String message) {
        //Validating error message in the response
        Assert.assertEquals(noAccessTokenResponse.getMessage(), message);
    }

    @When("User send request to get cart count with wrong userId")
    public void userSendRequestToGetCartCountWithWrongUserId() {
        cartResponse = given().spec(requestSpecification)
                .header("Authorization", loginAPIResponse.getToken())
                .pathParam("userId", "123sa")
                .when()
                .get("user/get-cart-count/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetCartCountAPI_NoCartFoundSchema.json")))
                .spec(getResponseSpecification(400, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetCartCountAPI_NoCartProductsResponse.class);
    }

    @Then("{string} error message is sent to user")
    public void errorMessageIsSentToUser(String message) {
        //Validating error message in the response
        Assert.assertEquals(cartResponse.getMessage(), message);
    }
}
