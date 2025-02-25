package StepDefinitions.APITest.LoginAPI;

import BaseTest.BaseTest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.LoginAPIResponse;
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
    LoginAPIResponse loginResponse;
    Response response;


    @Given("^User provides (.*) (.*) login credentials$")
    public void userProvidesUsernamePasswordLoginCredentials(String username, String password) {
        LoginAPIRequest loginRequest = new LoginAPIRequest();
        loginRequest.setUserEmail(username);
        loginRequest.setUserPassword(password);

        request = given().spec(requestSpecification).body(loginRequest).log().all();
    }

    @When("User sends request")
    public void userSendsRequest() {
        response = request.when()
                .post("/auth/login");

    }

    @Then("{string} message is returned")
    public void messageIsReturned(String message) {
        loginResponse = response.then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/LoginAPIIncorrectCredSchema.json")))
                .spec(getResponseSpecification(400, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(LoginAPIResponse.class);
        Assert.assertEquals(message, loginResponse.getMessage());
//        Assert.assertNotNull(loginResponse.getToken());
    }

    @When("User sends request with wrong end point")
    public void userSendsRequestWithWrongEndPoint() {
        response = request.when()
                .post("/auth/l");


    }

    @Then("{string} error is returned")
    public void errorIsReturned(String errorCode) {
        response.then()
                .spec(getResponseSpecification(404, responseTime, ContentType.HTML)).log().all();
    }

    @Then("{string} error is returned and {string} message is returned in body")
    public void errorIsReturnedAndMessageIsReturnedInBody(String errorCode, String message) {
        loginResponse = response.then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/LoginAPIEmptyFieldSchema.json")))
                .spec(getResponseSpecification(Integer.parseInt(errorCode), responseTime, ContentType.JSON)).log().all().extract().as(LoginAPIResponse.class);
        Assert.assertEquals(loginResponse.getMessage(), message);
    }


}
