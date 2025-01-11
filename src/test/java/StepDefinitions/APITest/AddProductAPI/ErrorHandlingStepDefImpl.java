package StepDefinitions.APITest.AddProductAPI;

import BaseTest.BaseTest;
import POJO.Request.AddProductAPIRequest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.AddProductAPI_ErrorResponse;
import POJO.Response.CreateOrderAPI_NoBodyResponse;
import POJO.Response.LoginAPIResponse;
import POJO.Response.NoAccessTokenResponse;
import com.github.javafaker.Faker;
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
    LoginAPIResponse loginAPIResponse;
    NoAccessTokenResponse noAccessTokenResponse;
    AddProductAPI_ErrorResponse addProductAPI_ErrorResponse;
    CreateOrderAPI_NoBodyResponse errorResponse;
    Response response;

    @Given("The user has valid credentials add product error handling")
    public void theUserHasValidCredentialsAddProductErrorHandling() {
        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();
    }

    @When("The user sends a login API request add product error handling")
    public void theUserSendsALoginAPIRequestAddProductErrorHandling() {
        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginAPIResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token add product error handling")
    public void theUserShouldReceiveAValidAuthenticationTokenAddProductErrorHandling() {
        //Verifying token is not null
        Assert.assertNotNull(loginAPIResponse.getToken());
    }

    @When("User send request to add product with wrong end point")
    public void userSendRequestToAddProductWithWrongEndPoint() {

        //Creating product api request body with valid details using faker library
        Faker faker = new Faker();

        AddProductAPIRequest requestBody = new AddProductAPIRequest(
                faker.app().name(),
                loginAPIResponse.getUserId(),
                "fashion",
                "shirts",
                faker.commerce().price(),
                faker.backToTheFuture().quote(),
                "women",
                new File("src/test/resources/Product Image.png")
        );

        //Sending add product api request to wrong end point
        response = given().spec(requestSpecification).contentType(ContentType.MULTIPART)
                .multiPart("productName", requestBody.getProductName())
                .multiPart("productAddedBy", requestBody.getProductAddedBy())
                .multiPart("productCategory", requestBody.getProductCategory())
                .multiPart("productSubCategory", requestBody.getProductSubCategory())
                .multiPart("productPrice", requestBody.getProductPrice())
                .multiPart("productDescription", requestBody.getProductDescription())
                .multiPart("productFor", requestBody.getProductFor())
                .multiPart("productImage", requestBody.getProductImage())
                .header("Authorization", loginAPIResponse.getToken()).log().all().when()
                .post("product/add");
    }

    @Then("User should receive {string} error code in Add product response")
    public void userShouldReceiveErrorCodeInAddProductResponse(String code) {
        response.then().spec(getResponseSpecification(Integer.parseInt(code), responseTime, ContentType.HTML)).log().all();

    }

    @When("User send request to add product with no auth token")
    public void userSendRequestToAddProductWithNoAuthToken() {
        //Creating product api request body with valid details using faker library
        Faker faker = new Faker();

        AddProductAPIRequest requestBody = new AddProductAPIRequest(
                faker.app().name(),
                loginAPIResponse.getUserId(),
                "fashion",
                "shirts",
                faker.commerce().price(),
                faker.backToTheFuture().quote(),
                "women",
                new File("src/test/resources/Product Image.png")
        );

        //Sending add product api request with no auth token
        noAccessTokenResponse = given().spec(requestSpecification).contentType(ContentType.MULTIPART)
                .multiPart("productName", requestBody.getProductName())
                .multiPart("productAddedBy", requestBody.getProductAddedBy())
                .multiPart("productCategory", requestBody.getProductCategory())
                .multiPart("productSubCategory", requestBody.getProductSubCategory())
                .multiPart("productPrice", requestBody.getProductPrice())
                .multiPart("productDescription", requestBody.getProductDescription())
                .multiPart("productFor", requestBody.getProductFor())
                .multiPart("productImage", requestBody.getProductImage())
                .log().all().when()
                .post("product/add-product")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/NoAccessTokenSchema.json")))
                .spec(getResponseSpecification(401, responseTime, ContentType.JSON)).log().all()
                .extract().as(NoAccessTokenResponse.class);
    }

    @Then("{string} message is sent to user in add product response")
    public void messageIsSentToUserInAddProductResponse(String message) {
        //Verifying message is sent to user in add product response
        Assert.assertEquals(message, noAccessTokenResponse.getMessage());
    }

    @When("^User send request to add products with no details (.+)$")
    public void userSendRequestToAddProductsWithNoDetailsField(String field) {
        //Creating product api request body with valid details using faker library
        Faker faker = new Faker();

        AddProductAPIRequest requestBody = new AddProductAPIRequest(
                faker.app().name(),
                loginAPIResponse.getUserId(),
                "fashion",
                "shirts",
                faker.commerce().price(),
                faker.backToTheFuture().quote(),
                "women",
                new File("src/test/resources/Product Image.png")
        );

        if (field.equalsIgnoreCase("productName"))
            requestBody.setProductName("");
        if (field.equalsIgnoreCase("productCategory"))
            requestBody.setProductCategory("");
        if (field.equalsIgnoreCase("productSubCategory"))
            requestBody.setProductSubCategory("");
        if (field.equalsIgnoreCase("productPrice"))
            requestBody.setProductPrice("");
        if (field.equalsIgnoreCase("productDescription"))
            requestBody.setProductDescription("");
        if (field.equalsIgnoreCase("all")) {
            requestBody = new AddProductAPIRequest("", "", "", "", "", "", "", new File("src/test/resources/Product Image.png"));
        }

//        System.out.println(requestBody);

        //Sending add product api request
        addProductAPI_ErrorResponse = given().spec(requestSpecification).contentType(ContentType.MULTIPART)
                .multiPart("productName", requestBody.getProductName())
                .multiPart("productAddedBy", requestBody.getProductAddedBy())
                .multiPart("productCategory", requestBody.getProductCategory())
                .multiPart("productSubCategory", requestBody.getProductSubCategory())
                .multiPart("productPrice", requestBody.getProductPrice())
                .multiPart("productDescription", requestBody.getProductDescription())
                .multiPart("productFor", requestBody.getProductFor())
                .multiPart("productImage", requestBody.getProductImage())
                .header("Authorization", loginAPIResponse.getToken()).log().all().when()
                .post("product/add-product")
                .then()
                .spec(getResponseSpecification(500, responseTime, ContentType.JSON)).log().all()
                .extract().as(AddProductAPI_ErrorResponse.class);
    }

    @Then("^(.+) message is sent to user in add products$")
    public void messageMessageIsSentToUserInAddProducts(String message) {
        //Validating error message
        Assert.assertEquals(message, addProductAPI_ErrorResponse.getMessage().getMessage());
    }

    @When("User send request to add product with no image")
    public void userSendRequestToAddProductWithNoImage() {
        //Creating product api request body with valid details using faker library
        Faker faker = new Faker();

        AddProductAPIRequest requestBody = new AddProductAPIRequest(
                faker.app().name(),
                loginAPIResponse.getUserId(),
                "fashion",
                "shirts",
                faker.commerce().price(),
                faker.backToTheFuture().quote(),
                "women",
                null
        );


//        System.out.println(requestBody);

        //Sending add product api request
        errorResponse = given().spec(requestSpecification).contentType(ContentType.MULTIPART)
                .multiPart("productName", requestBody.getProductName())
                .multiPart("productAddedBy", requestBody.getProductAddedBy())
                .multiPart("productCategory", requestBody.getProductCategory())
                .multiPart("productSubCategory", requestBody.getProductSubCategory())
                .multiPart("productPrice", requestBody.getProductPrice())
                .multiPart("productDescription", requestBody.getProductDescription())
                .multiPart("productFor", requestBody.getProductFor())
                .multiPart("productImage", "")
                .header("Authorization", loginAPIResponse.getToken()).log().all().when()
                .post("product/add-product")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/CreateorderAPI_NoBodySchema.json")))
                .spec(getResponseSpecification(500, responseTime, ContentType.JSON)).log().all()
                .extract().as(CreateOrderAPI_NoBodyResponse.class);
    }

    @Then("{string} is returned to user in add product response")
    public void isReturnedToUserInAddProductResponse(String message) {
        //Validating error message
        Assert.assertEquals(message, errorResponse.getType());
    }

    @When("User send request to add product with no body")
    public void userSendRequestToAddProductWithNoBody() {

        //Sending add product api request with no body
        errorResponse = given().spec(requestSpecification)
                .header("Authorization", loginAPIResponse.getToken()).log().all().when()
                .post("product/add-product")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/CreateorderAPI_NoBodySchema.json")))
                .spec(getResponseSpecification(500, responseTime, ContentType.JSON)).log().all()
                .extract().as(CreateOrderAPI_NoBodyResponse.class);
    }
}
