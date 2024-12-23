package StepDefinitions.APITest.LoginAPI;

import BaseTest.BaseTest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.LoginAPIResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

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
        loginResponse = response.then()
                .spec(getResponseSpecification(400, 2000, ContentType.JSON)).log().all()
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
                .spec(getResponseSpecification(404, 1000, ContentType.HTML)).log().all();
    }

    @Then("{string} error is returned and {string} message is returned in body")
    public void errorIsReturnedAndMessageIsReturnedInBody(String errorCode, String message) {
        loginResponse = response.then()
                .spec(getResponseSpecification(Integer.parseInt(errorCode), 1000, ContentType.JSON)).log().all().extract().as(LoginAPIResponse.class);
        Assert.assertEquals(loginResponse.getMessage(), message);
    }


}
