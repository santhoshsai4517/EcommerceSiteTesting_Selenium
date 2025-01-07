package StepDefinitions.APITest.LoginAPI;

import BaseTest.BaseTest;
import POJO.Request.LoginAPIRequest;
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
    LoginAPIResponse loginResponse;

    @Given("User provides correct login credentials")
    public void userProvidesCorrectLoginCredentials() {

        LoginAPIRequest loginRequest = new LoginAPIRequest();
        loginRequest.setUserEmail("santhoshsai4517@gmail.com");
        loginRequest.setUserPassword("151Fa04124@4517");

        request = given().spec(requestSpecification).body(loginRequest).log().all();

    }

    @When("User sends login request")
    public void userSendsLoginRequest() {
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(LoginAPIResponse.class);
    }

    @Then("{string} message is returned along with token")
    public void messageIsReturnedAlongWithToken(String message) {
        Assert.assertEquals(message, loginResponse.getMessage());
        Assert.assertNotNull(loginResponse.getToken());
    }

}
