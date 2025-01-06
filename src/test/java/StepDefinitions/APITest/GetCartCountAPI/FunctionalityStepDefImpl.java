package StepDefinitions.APITest.GetCartCountAPI;

import BaseTest.BaseTest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.GetCartCountAPI_NoCartProductsResponse;
import POJO.Response.LoginAPIResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;

import static io.restassured.RestAssured.given;

public class FunctionalityStepDefImpl extends BaseTest {

    RequestSpecification request;
    LoginAPIResponse loginAPIResponse;
    GetCartCountAPI_NoCartProductsResponse cartResponse;

    @Given("The user has valid credentials Get Cart Count")
    public void theUserHasValidCredentialsGetCartCount() {

        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();

    }

    @When("The user sends a login API request Get Cart Count")
    public void theUserSendsALoginAPIRequestGetCartCount() {

        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginAPIResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, 4000, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token Get Cart Count")
    public void theUserShouldReceiveAValidAuthenticationTokenGetCartCount() {
        //Verifying token is not null
        Assert.assertNotNull(loginAPIResponse.getToken());
    }

    @When("User send request to get cart count with userId")
    public void userSendRequestToGetCartCountWithUserId() {

        //Sending get cart count api request and extracting response as GetCartCountAPI_NoCartProductsResponse object and validating response code and response time
        cartResponse = given().spec(requestSpecification)
                .header("Authorization", loginAPIResponse.getToken())
                .pathParam("userId", loginAPIResponse.getUserId())
                .when()
                .get("user/get-cart-count/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetCartCountAPI_NoCartFoundSchema.json")))
                .spec(getResponseSpecification(200, 4000, ContentType.JSON)).log().all()
                .extract().as(GetCartCountAPI_NoCartProductsResponse.class);

    }


    @Then("User should receive {string} message")
    public void userShouldReceiveMessage(String message) {
        //Verifying message is as expected
        Assert.assertEquals(cartResponse.getMessage(), message);
    }


}
