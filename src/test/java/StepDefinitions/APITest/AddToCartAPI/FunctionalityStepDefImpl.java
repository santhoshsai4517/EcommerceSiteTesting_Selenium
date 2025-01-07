package StepDefinitions.APITest.AddToCartAPI;

import BaseTest.BaseTest;
import POJO.Request.AddToCartAPIRequest;
import POJO.Request.GetAllProductsAPIRequest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.AddToCartAPI_SuccessResponse;
import POJO.Response.GetAllProductsAPIResponse;
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
    AddToCartAPI_SuccessResponse addToCartAPIResponse;
    List<Product> products;

    @Given("The user has valid credentials add to cart")
    public void theUserHasValidCredentialsAddToCart() {

        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();

    }

    @When("The user sends a login API request add to cart")
    public void theUserSendsALoginAPIRequestAddToCart() {

        //Sending login api request body
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(LoginAPIResponse.class);

    }

    @Then("The user should receive a valid authentication token add to cart")
    public void theUserShouldReceiveAValidAuthenticationTokenAddToCart() {

        //Verifying the user should receive a valid authentication token
        Assert.assertNotNull(loginResponse.getToken());

    }

    @When("The user sends a request to get all products using the authentication token add to cart")
    public void theUserSendsARequestToGetAllProductsUsingTheAuthenticationTokenAddToCart() {

        //Creating request body for get all products api request
        GetAllProductsAPIRequest requestBody = new GetAllProductsAPIRequest();
        requestBody.setProductName("");
        requestBody.setMinPrice(null);
        requestBody.setMaxPrice(null);
        requestBody.setProductCategory(Collections.emptyList());
        requestBody.setProductSubCategory(Collections.emptyList());
        requestBody.setProductFor(Collections.emptyList());

        //Sending get all products api request body for get all products api request
        getAllProductsAPIResponse = given().spec(requestSpecification).body(requestBody)
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .post("/product/get-all-products")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetAllProductsAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(GetAllProductsAPIResponse.class);
    }

    @Then("The user should receive a list of product IDs add to cart")
    public void theUserShouldReceiveAListOfProductIDsAddToCart() {
        //Extracting product details
        products = getAllProductsAPIResponse.getData();

    }

    @When("The user sends a request to add product to cart for each product ID using the authentication token")
    public void theUserSendsARequestToAddProductToCartForEachProductIDUsingTheAuthenticationToken() {
        //Creating add to cart request body
        AddToCartAPIRequest requestBody = new AddToCartAPIRequest();
        requestBody.setProduct(products.get(0));
        requestBody.set_id(loginResponse.getUserId());

        //Sending add to cart api request body
        addToCartAPIResponse = given().spec(requestSpecification).body(requestBody)
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .post("/user/add-to-cart")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/AddToCartAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(AddToCartAPI_SuccessResponse.class);
    }

    @Then("{string} message is returned in add to cart response")
    public void messageIsReturnedInAddToCartResponse(String message) {
        //Validating the message
        Assert.assertEquals(message, addToCartAPIResponse.getMessage());
    }


}
