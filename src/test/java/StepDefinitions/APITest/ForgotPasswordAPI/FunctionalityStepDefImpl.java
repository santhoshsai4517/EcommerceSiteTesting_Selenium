package StepDefinitions.APITest.ForgotPasswordAPI;


import BaseTest.BaseTest;
import POJO.Request.ForgotPasswordAPIRequest;
import POJO.Response.ForgotPasswordAPIResponse;
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
    ForgotPasswordAPIResponse forgotPasswordAPIResponse;

    @Given("User provides correct details")
    public void userProvidesCorrectDetails() {

        ForgotPasswordAPIRequest forgotPasswordAPIRequest = new ForgotPasswordAPIRequest();
        forgotPasswordAPIRequest.setUserEmail("s1234@gmail.com");
        forgotPasswordAPIRequest.setUserPassword("151Fa04124@4517");
        forgotPasswordAPIRequest.setConfirmPassword("151Fa04124@4517");

        request = given().spec(requestSpecification).body(forgotPasswordAPIRequest).log().all();

    }

    @When("User sends forgot password request")
    public void userSendsForgotPasswordRequest() {
        forgotPasswordAPIResponse = request.when()
                .post("/auth/new-password")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/ForgotPasswordAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, 2000, ContentType.JSON)).log().all()
                .extract()
                .as(ForgotPasswordAPIResponse.class);
    }

    @Then("{string} message is returned to user")
    public void messageIsReturnedToUser(String message) {
        Assert.assertEquals(message, forgotPasswordAPIResponse.getMessage());
    }


}
