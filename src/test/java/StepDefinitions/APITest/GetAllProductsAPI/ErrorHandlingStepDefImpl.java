package StepDefinitions.APITest.GetAllProductsAPI;

import BaseTest.BaseTest;
import POJO.Request.GetAllProductsAPIRequest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.GetAllProductsAPIResponse;
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
import java.util.Collections;

import static io.restassured.RestAssured.given;

public class ErrorHandlingStepDefImpl extends BaseTest {

    RequestSpecification request;
    GetAllProductsAPIResponse getAllProductsAPIResponse;
    NoAccessTokenResponse noAccessTokenResponse;

    LoginAPIResponse loginResponse;
    Response response;

    @Given("User provides filter details")
    public void userProvidesFilterDetails() {
        GetAllProductsAPIRequest requestBody = new GetAllProductsAPIRequest();
        requestBody.setProductName("");
        requestBody.setMinPrice(null);
        requestBody.setMaxPrice(null);
        requestBody.setProductCategory(Collections.emptyList());
        requestBody.setProductSubCategory(Collections.emptyList());
        requestBody.setProductFor(Collections.emptyList());

        LoginAPIRequest loginRequest = new LoginAPIRequest();
        loginRequest.setUserEmail("santhoshsai4517@gmail.com");
        loginRequest.setUserPassword("151Fa04124@4517");

        loginResponse = given().spec(requestSpecification).body(loginRequest).log().all().when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, 2000, ContentType.JSON)).log().all()
                .extract()
                .as(LoginAPIResponse.class);

        request = given().spec(requestSpecification).body(requestBody)
                .header("Authorization", loginResponse.getToken()).log().all();
    }

    @When("User sends request with wrong api end point")
    public void userSendsRequestWithWrongApiEndPoint() {
        response = request.when()
                .post("/auth/et-all-products");
    }

    @Then("{string} error is returned in response")
    public void errorIsReturnedInResponse(String error) {
        response.then()
                .spec(getResponseSpecification(Integer.parseInt(error), 2000, ContentType.HTML)).log().all();
    }

    @Given("User provides filter details without authorization token")
    public void userProvidesFilterDetailsWithoutAuthorizationToken() {
        GetAllProductsAPIRequest requestBody = new GetAllProductsAPIRequest();
        requestBody.setProductName("");
        requestBody.setMinPrice(null);
        requestBody.setMaxPrice(null);
        requestBody.setProductCategory(Collections.emptyList());
        requestBody.setProductSubCategory(Collections.emptyList());
        requestBody.setProductFor(Collections.emptyList());


        request = given().spec(requestSpecification).body(requestBody).log().all();
    }

    @When("User sends request with api end point")
    public void userSendsRequestWithApiEndPoint() {
        noAccessTokenResponse = request.when()
                .post("/product/get-all-products")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/NoAccessTokenSchema.json")))
                .spec(getResponseSpecification(401, 2000, ContentType.JSON)).log().all()
                .extract()
                .as(NoAccessTokenResponse.class);
    }

    @Then("{string} message is returned in response")
    public void messageIsReturnedInResponse(String message) {
        Assert.assertEquals(message, noAccessTokenResponse.getMessage());
    }
}
