package StepDefinitions.APITest.GetProductDetailsAPI;

import BaseTest.BaseTest;
import POJO.Request.GetAllProductsAPIRequest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.GetAllProductsAPIResponse;
import POJO.Response.GetProductDetailsResponse;
import POJO.Response.LoginAPIResponse;
import POJO.Response.Product;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class FunctionalityStepDefImpl extends BaseTest {

    RequestSpecification request;
    LoginAPIResponse loginResponse;
    GetAllProductsAPIResponse getAllProductsAPIResponse;
    GetProductDetailsResponse getProductDetailsResponse;
    List<Product> products;

    @Given("The user has valid credentials")
    public void theUserHasValidCredentials() {
        LoginAPIRequest loginRequest = new LoginAPIRequest();
        loginRequest.setUserEmail("santhoshsai4517@gmail.com");
        loginRequest.setUserPassword("151Fa04124@4517");

        request = given().spec(requestSpecification).body(loginRequest).log().all();
    }

    @When("The user sends a login API request")
    public void theUserSendsALoginAPIRequest() {
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token")
    public void theUserShouldReceiveAValidAuthenticationToken() {
        Assert.assertNotNull(loginResponse.getToken());
    }

    @When("The user sends a request to get all products using the authentication token")
    public void theUserSendsARequestToGetAllProductsUsingTheAuthenticationToken() {

        GetAllProductsAPIRequest requestBody = new GetAllProductsAPIRequest();
        requestBody.setProductName("");
        requestBody.setMinPrice(null);
        requestBody.setMaxPrice(null);
        requestBody.setProductCategory(Collections.emptyList());
        requestBody.setProductSubCategory(Collections.emptyList());
        requestBody.setProductFor(Collections.emptyList());

        getAllProductsAPIResponse = given().spec(requestSpecification).body(requestBody)
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .post("/product/get-all-products")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetAllProductsAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(GetAllProductsAPIResponse.class);


    }

    @Then("The user should receive a list of product IDs")
    public void theUserShouldReceiveAListOfProductIDs() {
        products = getAllProductsAPIResponse.getData();
    }


    @When("The user sends a request to get product details for each product ID using the authentication token and {string} message is returned in response")
    public void theUserSendsARequestToGetProductDetailsForEachProductIDUsingTheAuthenticationTokenAndMessageIsReturnedInResponse(String message) {
        for (Product product : products) {
            getProductDetailsResponse = given().spec(requestSpecification).pathParam("productId", product.get_id())
                    .header("Authorization", loginResponse.getToken()).log().all().when()
                    .get("/product/get-product-detail/{productId}")
                    .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetProductDetails_SuccessSchema.json")))
                    .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                    .extract()
                    .as(GetProductDetailsResponse.class);
            Assert.assertEquals(getProductDetailsResponse.getMessage(), message);
            Assert.assertEquals(getProductDetailsResponse.getData().get_id(), product.get_id());
            Assert.assertEquals(getProductDetailsResponse.getData().getProductName(), product.getProductName());
        }
    }
}
