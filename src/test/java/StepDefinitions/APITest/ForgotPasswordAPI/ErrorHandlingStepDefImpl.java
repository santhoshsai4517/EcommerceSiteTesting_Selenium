package StepDefinitions.APITest.ForgotPasswordAPI;

import BaseTest.BaseTest;
import POJO.Request.ForgotPasswordAPIRequest;
import POJO.Response.ForgotPasswordAPIResponse;
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
    Response response;
    ForgotPasswordAPIResponse forgotPasswordAPIResponse;

    @Given("^User provides (.+) (.*) (.*) details$")
    public void userProvidesUsernamePasswordConfirmpasswordDetails(String email, String password, String confirmPassword) {

        ForgotPasswordAPIRequest forgotPasswordAPIRequest = new ForgotPasswordAPIRequest();
        forgotPasswordAPIRequest.setUserEmail(email);
        forgotPasswordAPIRequest.setUserPassword(password);
        forgotPasswordAPIRequest.setConfirmPassword(confirmPassword);

        request = given().spec(requestSpecification).body(forgotPasswordAPIRequest).log().all();

    }

    @When("User sends forgot password api request")
    public void userSendsForgotPasswordApiRequest() {
        response = request.when()
                .post("/auth/new-password");

    }

    @Then("^(.+) message  and (.+) error code is returned$")
    public void messageMessageAndCodeErrorCodeIsReturned(String message, String code) {
        forgotPasswordAPIResponse = response.then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/ForgotPasswordAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(Integer.parseInt(code), responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(ForgotPasswordAPIResponse.class);

        Assert.assertEquals(message, forgotPasswordAPIResponse.getMessage());
    }

    @When("User sends request to wrong end point")
    public void userSendsRequestToWrongEndPoint() {
        response = request.when()
                .post("/auth/new-passwordcer");
    }

    @Then("{string} error code is returned in response")
    public void errorIsReturnedInResponse(String code) {
        response.then()
                .spec(getResponseSpecification(Integer.parseInt(code), responseTime, ContentType.HTML)).log().all();
    }


}
