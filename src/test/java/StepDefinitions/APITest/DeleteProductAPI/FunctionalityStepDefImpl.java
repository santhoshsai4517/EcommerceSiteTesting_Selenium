package StepDefinitions.APITest.DeleteProductAPI;

import BaseTest.BaseTest;
import POJO.Request.GetAllProductsAPIRequest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.DeleteProductAPI_SuccessResponse;
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
    LoginAPIResponse loginAPIResponse;
    GetAllProductsAPIResponse getAllProductsAPIResponse;
    DeleteProductAPI_SuccessResponse deleteProductAPIResponse;
    List<Product> products;

    @Given("The user has valid credentials delete product")
    public void theUserHasValidCredentialsDeleteProduct() {
        //Creating login api request body
        LoginAPIRequest requestBody = new LoginAPIRequest();
        requestBody.setUserEmail("santhoshsai4517@gmail.com");
        requestBody.setUserPassword("151Fa04124@4517");

        //Creating request specification for login api request
        request = given().spec(requestSpecification).body(requestBody).log().all();
    }

    @When("The user sends a login API request delete product")
    public void theUserSendsALoginAPIRequestDeleteProduct() {
        //Sending login api request and extracting response as LoginAPIResponse object and validating response code and response time
        loginAPIResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(LoginAPIResponse.class);
    }

    @Then("The user should receive a valid authentication token delete product")
    public void theUserShouldReceiveAValidAuthenticationTokenDeleteProduct() {
        //Validating token is not null
        Assert.assertNotNull(loginAPIResponse.getToken());
    }

    @When("User sends get products api request with valid userID delete product")
    public void userSendsGetProductsApiRequestWithValidUserIDDeleteProduct() {
        //Creating get products api body
        GetAllProductsAPIRequest requestBody = new GetAllProductsAPIRequest();
        requestBody.setProductName("");
        requestBody.setMinPrice(null);
        requestBody.setMaxPrice(null);
        requestBody.setProductCategory(Collections.emptyList());
        requestBody.setProductSubCategory(Collections.emptyList());
        requestBody.setProductFor(Collections.emptyList());

        //Sending get products api request
        getAllProductsAPIResponse = given().spec(requestSpecification).body(requestBody)
                .header("Authorization", loginAPIResponse.getToken()).log().all().when()
                .post("/product/get-all-products")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetAllProductsAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(GetAllProductsAPIResponse.class);
    }

    @Then("The user should receive order details get products delete product")
    public void theUserShouldReceiveOrderDetailsGetProductsDeleteProduct() {
        //Extracting products
        products = getAllProductsAPIResponse.getData();
    }

    @When("User sends valid product id to delete")
    public void userSendsValidProductIdToDelete() {
        for (Product product : products) {
            if (product.getProductAddedBy().equals(loginAPIResponse.getUserId())) {
                //Deleting product
                deleteProductAPIResponse = given().spec(requestSpecification).header("Authorization", loginAPIResponse.getToken())
                        .pathParam("productId", product.get_id()).log().all().when()
                        .delete("product/delete-product/{productId}")
                        .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/DeleteProductAPI_SuccessSchema.json")))
                        .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                        .extract()
                        .as(DeleteProductAPI_SuccessResponse.class);
                break;
            }
        }
    }

    @Then("User receives {string} message in delete product api response")
    public void userReceivesMessageInDeleteProductApiResponse(String message) {
        //Validating message in response
        Assert.assertEquals(deleteProductAPIResponse.getMessage(), message);
    }

}
