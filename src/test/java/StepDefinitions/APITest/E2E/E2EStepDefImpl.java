package StepDefinitions.APITest.E2E;

import BaseTest.BaseTest;
import POJO.Request.ForgotPasswordAPIRequest;
import POJO.Request.GetAllProductsAPIRequest;
import POJO.Request.LoginAPIRequest;
import POJO.Request.RegisterAPIRequest;
import POJO.Response.*;
import com.github.javafaker.Faker;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class E2EStepDefImpl extends BaseTest {

    RequestSpecification request;
    LoginAPIResponse loginResponse;
    ForgotPasswordAPIResponse forgotPasswordAPIResponse;
    RegisterAPIResponse registerResponse;
    GetAllProductsAPIResponse getAllProductsAPIResponse;
    GetProductDetailsResponse getProductDetailsResponse;

    List<Product> products;
    List<Product> productsToAdd = new ArrayList<>();
    int count = 3;

    Faker faker = new Faker(); // Initializing Faker to generate fake data
    String email = faker.internet().emailAddress();
    String password = "151Fa04124@4517";
    String updatedPassword = faker.internet().password();

    @When("User sends register request with all valid details")
    public void userSendsRegisterRequestWithAllValidDetails() {

        //Creating register api request body
        RegisterAPIRequest registerAPIRequest = new RegisterAPIRequest();
        registerAPIRequest.setFirstName(faker.name().firstName());
        registerAPIRequest.setLastName(faker.name().lastName());
        registerAPIRequest.setUserEmail(email);
        registerAPIRequest.setUserPassword(password);
        registerAPIRequest.setConfirmPassword(password);
        registerAPIRequest.setGender("Male");
        registerAPIRequest.setUserMobile("9988776655");
        registerAPIRequest.setOccupation("Doctor");
        registerAPIRequest.setUserRole("customer");
        registerAPIRequest.setRequired(true);

        //Sending register request and validating response schema
        registerResponse = given().spec(requestSpecification).body(registerAPIRequest).log().all()
                .when()
                .post("/auth/register")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/RegisterAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(RegisterAPIResponse.class);
    }

    @Then("{string} message is returned in register response")
    public void messageIsReturnedInRegisterResponse(String message) {
        //Validating the message
        Assert.assertEquals(message, registerResponse.getMessage());
    }

    @When("User sends forgot password request with all valid details")
    public void userSendsForgotPasswordRequestWithAllValidDetails() {

        //Creating forgot password api request body
        ForgotPasswordAPIRequest forgotPasswordAPIRequest = new ForgotPasswordAPIRequest();
        forgotPasswordAPIRequest.setUserEmail(email);
        forgotPasswordAPIRequest.setUserPassword(updatedPassword);
        forgotPasswordAPIRequest.setConfirmPassword(updatedPassword);

        //Sending forgot password api request
        forgotPasswordAPIResponse = given().spec(requestSpecification).body(forgotPasswordAPIRequest).log().all()
                .when()
                .post("/auth/new-password")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/ForgotPasswordAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(ForgotPasswordAPIResponse.class);
    }

    @Then("{string} message is returned to user in forgot password api response")
    public void messageIsReturnedToUserInForgotPasswordApiResponse(String message) {
        //Validating the message
        Assert.assertEquals(message, forgotPasswordAPIResponse.getMessage());
    }

    @When("user sends login api request with valid details")
    public void userSendsLoginApiRequestWithValidDetails() {
        //Creating login api request body
        LoginAPIRequest loginAPIRequest = new LoginAPIRequest();
        loginAPIRequest.setUserEmail(email);
        loginAPIRequest.setUserPassword(updatedPassword);

        //Sending login request and validating response schema
        loginResponse = given().spec(requestSpecification).body(loginAPIRequest).log().all()
                .when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(LoginAPIResponse.class);
    }

    @Then("{string} message is returned along with token and user ID")
    public void messageIsReturnedAlongWithTokenAndUserID(String message) {
        //Validating the message and token
        Assert.assertEquals(message, loginResponse.getMessage());
        Assert.assertNotNull(loginResponse.getToken());
        Assert.assertNotNull(loginResponse.getUserId());
    }


    @When("User sends get all products api request without any filters")
    public void userSendsGetAllProductsApiRequestWithoutAnyFilters() {

        //Creating get products api body without any filters
        GetAllProductsAPIRequest requestBody = new GetAllProductsAPIRequest();
        requestBody.setProductName("");
        requestBody.setMinPrice(null);
        requestBody.setMaxPrice(null);
        requestBody.setProductCategory(Collections.emptyList());
        requestBody.setProductSubCategory(Collections.emptyList());
        requestBody.setProductFor(Collections.emptyList());

        //Sending get products request and validating response schema
        getAllProductsAPIResponse = given().spec(requestSpecification).body(requestBody)
                .header("Authorization", loginResponse.getToken()).log().all()
                .when()
                .post("/product/get-all-products")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetAllProductsAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(GetAllProductsAPIResponse.class);

    }

    @Then("{string} message is returned along with product list in get all products api response")
    public void messageIsReturnedAlongWithProductListInGetAllProductsApiResponse(String message) {
        //Validating the message and product list
        Assert.assertEquals(message, getAllProductsAPIResponse.getMessage());
        Assert.assertFalse(getAllProductsAPIResponse.getData().isEmpty());

        //Extracting products
        products = getAllProductsAPIResponse.getData();
    }

    @When("User sends get product details request with valid product id")
    public void userSendsGetProductDetailsRequestWithValidProductId() {
        //Creating a list of product IDs
        while (productsToAdd.size() < count) {
            Product product = products.get(new Random().nextInt(products.size()));
            if (!productsToAdd.contains(product)) {
                productsToAdd.add(product);
                System.out.println(product);
            }
        }

        for (Product product : productsToAdd) {
            getProductDetailsResponse = given().spec(requestSpecification).pathParam("productId", product.get_id())
                    .header("Authorization", loginResponse.getToken()).log().all().when()
                    .get("/product/get-product-detail/{productId}")
                    .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetProductDetails_SuccessSchema.json")))
                    .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                    .extract()
                    .as(GetProductDetailsResponse.class);

            Assert.assertEquals(getProductDetailsResponse.getData().get_id(), product.get_id());
            Assert.assertEquals(getProductDetailsResponse.getData().getProductName(), product.getProductName());
        }

    }

    @Then("{string} message is returned along with product details in get product by id api response")
    public void messageIsReturnedAlongWithProductDetailsInGetProductByIdApiResponse(String message) {
        Assert.assertEquals(getProductDetailsResponse.getMessage(), message);
    }
}
