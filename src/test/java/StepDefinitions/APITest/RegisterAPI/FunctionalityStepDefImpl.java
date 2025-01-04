package StepDefinitions.APITest.RegisterAPI;

import BaseTest.BaseTest;
import POJO.Request.RegisterAPIRequest;
import POJO.Response.RegisterAPIResponse;
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
    RegisterAPIResponse registerAPIResponse;

    @Given("User provides correct detials")
    public void userProvidesCorrectDetials() {

        RegisterAPIRequest registerAPIRequest = new RegisterAPIRequest();
        registerAPIRequest.setFirstName("sai");
        registerAPIRequest.setLastName("santhosh");
        registerAPIRequest.setUserEmail("dsgbdfeornnlkmn@gmail.com");
        registerAPIRequest.setUserPassword("151Fa04124@4517");
        registerAPIRequest.setConfirmPassword("151Fa04124@4517");
        registerAPIRequest.setGender("Male");
        registerAPIRequest.setUserMobile("9988776655");
        registerAPIRequest.setOccupation("Doctor");
        registerAPIRequest.setUserRole("customer");
        registerAPIRequest.setRequired(true);

        request = given().spec(requestSpecification).body(registerAPIRequest).log().all();

    }

    @When("User sends register request")
    public void userSendsRegisterRequest() {

        registerAPIResponse = request.when()
                .post("/auth/register")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/RegisterAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, 2000, ContentType.JSON)).log().all()
                .extract()
                .as(RegisterAPIResponse.class);

    }

    @Then("{string} message is returned in register api response")
    public void messageIsReturnedInRegisterApiResponse(String message) {
        Assert.assertEquals(message, registerAPIResponse.getMessage());
    }

}
