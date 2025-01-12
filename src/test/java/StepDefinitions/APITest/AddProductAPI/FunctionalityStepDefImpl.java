package StepDefinitions.APITest.AddProductAPI;

import BaseTest.BaseTest;
import POJO.Request.AddProductAPIRequest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.AddProductAPI_SuccessResponse;
import POJO.Response.LoginAPIResponse;
import com.github.javafaker.Faker;
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
    AddProductAPI_SuccessResponse addProductAPIResponse;


    @Given("The user has valid credentials add product")
    public void theUserHasValidCredentialsAddProduct() {
        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();
    }

    @When("The user sends a login API request add product")
    public void theUserSendsALoginAPIRequestAddProduct() {
        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token add product")
    public void theUserShouldReceiveAValidAuthenticationTokenAddProduct() {
        //Verifying the user should receive a valid authentication token
        Assert.assertNotNull(loginResponse.getToken());
    }

    @When("The user sends a request to add product with all valid details")
    public void theUserSendsARequestToAddProductWithAllValidDetails() {

        //Creating product api request body with valid details using faker library
        Faker faker = new Faker();

        AddProductAPIRequest requestBody = new AddProductAPIRequest(
                faker.app().name(),
                loginResponse.getUserId(),
                "fashion",
                "shirts",
                faker.commerce().price(),
                faker.backToTheFuture().quote(),
                "women",
                new File("src/test/resources/Product Image.png")
        );

        //Sending add product api request
        addProductAPIResponse = given().spec(requestSpecification).contentType(ContentType.MULTIPART)
                .multiPart("productName", requestBody.getProductName())
                .multiPart("productAddedBy", requestBody.getProductAddedBy())
                .multiPart("productCategory", requestBody.getProductCategory())
                .multiPart("productSubCategory", requestBody.getProductSubCategory())
                .multiPart("productPrice", requestBody.getProductPrice())
                .multiPart("productDescription", requestBody.getProductDescription())
                .multiPart("productFor", requestBody.getProductFor())
                .multiPart("productImage", requestBody.getProductImage())
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .post("product/add-product")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/AddProductAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(201, responseTime, ContentType.JSON)).log().all()
                .extract().as(AddProductAPI_SuccessResponse.class);
    }

    @Then("{string} message is returned along with product id")
    public void messageIsReturnedAlongWithProductId(String message) {
        //Validating message from response
        Assert.assertEquals(message, addProductAPIResponse.getMessage());
        //Validating product id in response
        Assert.assertNotNull(addProductAPIResponse.getProductId());
    }


}
