package StepDefinitions.APITest.RegisterAPI;

import BaseTest.BaseTest;
import POJO.Request.RegisterAPIRequest;
import POJO.Response.RegisterAPIErrorResponse;
import POJO.Response.RegisterAPIResponse;
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
    RegisterAPIErrorResponse registerAPIResponse;
    RegisterAPIResponse registerResponse;
    Response response;

    @Given("^User provides (.*) invalid email address$")
    public void userProvidesEmailInvalidEmailAddress(String email) {

        RegisterAPIRequest registerAPIRequest = new RegisterAPIRequest();
        registerAPIRequest.setFirstName("sai");
        registerAPIRequest.setLastName("santhosh");
        registerAPIRequest.setUserEmail(email);
        registerAPIRequest.setUserPassword("151Fa04124@4517");
        registerAPIRequest.setConfirmPassword("151Fa04124@4517");
        registerAPIRequest.setGender("Male");
        registerAPIRequest.setUserMobile("9988776655");
        registerAPIRequest.setOccupation("Doctor");
        registerAPIRequest.setUserRole("customer");
        registerAPIRequest.setRequired(true);

        request = given().spec(requestSpecification).body(registerAPIRequest).log().all();

    }

    @When("User sends register api request")
    public void userSendsRegisterApiRequest() {

        registerAPIResponse = request.when()
                .post("/auth/register")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/RegisterAPI_ErrorSchema.json")))
                .spec(getResponseSpecification(422, 2000, ContentType.JSON)).log().all()
                .extract()
                .as(RegisterAPIErrorResponse.class);

    }

    @Then("{string} error message is returned in register api response")
    public void errorMessageIsReturnedInRegisterApiResponse(String error) {
        Assert.assertEquals(error, registerAPIResponse.getError());
        Assert.assertFalse(registerAPIResponse.isSuccess());
    }


    @Given("^User provides (.*) invalid phone number$")
    public void userProvidesMobileInvalidPhoneNumber(String mobile) {

        RegisterAPIRequest registerAPIRequest = new RegisterAPIRequest();
        registerAPIRequest.setFirstName("sai");
        registerAPIRequest.setLastName("santhosh");
        registerAPIRequest.setUserEmail("s1234@gmail.com");
        registerAPIRequest.setUserPassword("151Fa04124@4517");
        registerAPIRequest.setConfirmPassword("151Fa04124@4517");
        registerAPIRequest.setGender("Male");
        registerAPIRequest.setUserMobile(mobile);
        registerAPIRequest.setOccupation("Doctor");
        registerAPIRequest.setUserRole("customer");
        registerAPIRequest.setRequired(true);

        request = given().spec(requestSpecification).body(registerAPIRequest).log().all();

    }

    @Then("^(.+) error is returned in register api response$")
    public void errorErrorIsReturned(String error) {
        Assert.assertFalse(registerAPIResponse.isSuccess());
        Assert.assertEquals(error, registerAPIResponse.getError());
    }

    @Given("^User provides (.*) invalid password$")
    public void userProvidesPasswordInvalidPassword(String password) {
        RegisterAPIRequest registerAPIRequest = new RegisterAPIRequest();
        registerAPIRequest.setFirstName("sai");
        registerAPIRequest.setLastName("santhosh");
        registerAPIRequest.setUserEmail("s1234@gmail.com");
        registerAPIRequest.setUserPassword(password);
        registerAPIRequest.setConfirmPassword("151Fa04124@4517");
        registerAPIRequest.setGender("Male");
        registerAPIRequest.setUserMobile("1234567890");
        registerAPIRequest.setOccupation("Doctor");
        registerAPIRequest.setUserRole("customer");
        registerAPIRequest.setRequired(true);

        request = given().spec(requestSpecification).body(registerAPIRequest).log().all();
    }


    @Given("User provides invalid email address")
    public void userProvidesInvalidEmailAddress() {
        RegisterAPIRequest registerAPIRequest = new RegisterAPIRequest();
        registerAPIRequest.setFirstName("sai");
        registerAPIRequest.setLastName("santhosh");
        registerAPIRequest.setUserEmail("s1234@gmail.com");
        registerAPIRequest.setUserPassword("151Fa04124@4517");
        registerAPIRequest.setConfirmPassword("151Fa04124@4517");
        registerAPIRequest.setGender("Male");
        registerAPIRequest.setUserMobile("1234567890");
        registerAPIRequest.setOccupation("Doctor");
        registerAPIRequest.setUserRole("customer");
        registerAPIRequest.setRequired(true);

        request = given().spec(requestSpecification).body(registerAPIRequest).log().all();
    }

    @When("User sends api request")
    public void userSendsApiRequest() {
        registerResponse = request.when()
                .post("/auth/register")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/RegisterAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(400, 2000, ContentType.JSON)).log().all()
                .extract()
                .as(RegisterAPIResponse.class);
    }

    @Then("Error {string}  is returned in register api response")
    public void errorIsReturnedInRegisterApiResponse(String error) {
        Assert.assertEquals(error, registerResponse.getMessage());
    }

    @Given("^User provides (.+) as empty$")
    public void userProvidesFieldAsEmpty(String field) {

        RegisterAPIRequest registerAPIRequest = new RegisterAPIRequest();
        if (!field.equalsIgnoreCase("fn"))
            registerAPIRequest.setFirstName("sai");
        if (!field.equalsIgnoreCase("ln"))
            registerAPIRequest.setLastName("santhosh");
        registerAPIRequest.setUserEmail("s1234@gmail.com");
        registerAPIRequest.setUserPassword("151Fa04124@4517");
        registerAPIRequest.setConfirmPassword("151Fa04124@4517");
        registerAPIRequest.setGender("Male");
        registerAPIRequest.setUserMobile("1234567890");
        registerAPIRequest.setOccupation("Doctor");
        registerAPIRequest.setUserRole("customer");
        registerAPIRequest.setRequired(true);

        request = given().spec(requestSpecification).body(registerAPIRequest).log().all();

    }

    @Given("User provides all correct details")
    public void userProvidesAllCorrectDetails() {
        RegisterAPIRequest registerAPIRequest = new RegisterAPIRequest();
        registerAPIRequest.setFirstName("sai");
        registerAPIRequest.setLastName("santhosh");
        registerAPIRequest.setUserEmail("s1234@gmail.com");
        registerAPIRequest.setUserPassword("151Fa04124@4517");
        registerAPIRequest.setConfirmPassword("151Fa04124@4517");
        registerAPIRequest.setGender("Male");
        registerAPIRequest.setUserMobile("1234567890");
        registerAPIRequest.setOccupation("Doctor");
        registerAPIRequest.setUserRole("customer");
        registerAPIRequest.setRequired(true);

        request = given().spec(requestSpecification).body(registerAPIRequest).log().all();
    }

    @When("User sends request with wrong register api end point")
    public void userSendsRequestWithWrongRegisterApiEndPoint() {
        response = request.when()
                .post("/auth/registvvrer");

    }

    @Then("{string} error is returned in register api reesponse")
    public void errorIsReturnedInRegisterApiReesponse(String code) {
        response.then()
                .spec(getResponseSpecification(Integer.parseInt(code), 2000, ContentType.HTML)).log().all();

    }
}
