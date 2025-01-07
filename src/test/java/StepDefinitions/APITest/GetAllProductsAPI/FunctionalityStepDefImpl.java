package StepDefinitions.APITest.GetAllProductsAPI;

import BaseTest.BaseTest;
import POJO.Request.GetAllProductsAPIRequest;
import POJO.Request.LoginAPIRequest;
import POJO.Response.GetAllProductsAPIResponse;
import POJO.Response.LoginAPIResponse;
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

    @Given("User provides login information")
    public void userProvidesLoginInformation() {

        LoginAPIRequest loginRequest = new LoginAPIRequest();
        loginRequest.setUserEmail("santhoshsai4517@gmail.com");
        loginRequest.setUserPassword("151Fa04124@4517");

        request = given().spec(requestSpecification).body(loginRequest).log().all();
    }

    @When("User sends login api request")
    public void userSendsLoginApiRequest() {
        loginResponse = request.when()
                .post("/auth/login")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/LoginAPISuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(LoginAPIResponse.class);
    }

    @Then("User gets back a token in api response")
    public void userGetsBackATokenInApiResponse() {
        Assert.assertNotNull(loginResponse.getToken());
    }


    @Given("^User provides (.*) (.*) (.*) (.*) (.*) (.*) filter details to get products$")
    public void userProvidesNameMinMaxCatSubcatForFilterDetailsToGetProducts(String name, String min, String max, String cat, String subcat, String gender) {

        GetAllProductsAPIRequest requestBody = new GetAllProductsAPIRequest();
        requestBody.setProductName("");
        requestBody.setMinPrice(null);
        requestBody.setMaxPrice(null);
        requestBody.setProductCategory(Collections.emptyList());
        requestBody.setProductSubCategory(Collections.emptyList());
        requestBody.setProductFor(Collections.emptyList());

        if (!name.isEmpty())
            requestBody.setProductName(name);
        if (!min.isEmpty())
            requestBody.setMinPrice(Integer.parseInt(min));
        if (!max.isEmpty())
            requestBody.setMaxPrice(Integer.parseInt(max));
        if (!cat.isEmpty()) {
            List<String> category = List.of(cat.split(","));
            requestBody.setProductCategory(category);
        }
        if (!subcat.isEmpty()) {
            List<String> subCategory = List.of(subcat.split(","));
            requestBody.setProductSubCategory(subCategory);
        }
        if (!gender.isEmpty())
            requestBody.setProductFor(Collections.singletonList(gender));

        request = given().spec(requestSpecification).body(requestBody)
                .header("Authorization", loginResponse.getToken()).log().all();
    }

    @When("User sends get all products api request")
    public void userSendsGetAllProductsApiReques() {
        getAllProductsAPIResponse = request.when()
                .post("/product/get-all-products")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetAllProductsAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(GetAllProductsAPIResponse.class);
    }

    @Then("{string} message is returned in get all products api response")
    public void messageIsReturnedInGetallProductsApiResponse(String message) {
        Assert.assertEquals(message, getAllProductsAPIResponse.getMessage());
        Assert.assertEquals(getAllProductsAPIResponse.getCount(), getAllProductsAPIResponse.getData().size());
    }


    @Given("User provides tshirt filter")
    public void userProvidesTshirtFilter() {

        GetAllProductsAPIRequest requestBody = new GetAllProductsAPIRequest();
        requestBody.setProductName("");
        requestBody.setMinPrice(null);
        requestBody.setMaxPrice(null);
        requestBody.setProductCategory(Collections.emptyList());
        requestBody.setProductSubCategory(List.of("t-shirts"));
        requestBody.setProductFor(Collections.emptyList());


        request = given().spec(requestSpecification).body(requestBody)
                .header("Authorization", loginResponse.getToken()).log().all();

    }

    @When("User sends get products api request")
    public void userSendsGetProductsApiRequest() {
        getAllProductsAPIResponse = request.when()
                .post("/product/get-all-products")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetAllProductsAPI_NoProductsSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(GetAllProductsAPIResponse.class);
    }

    @Then("{string} message is returned wiht no products")
    public void messageIsReturnedWihtNoProducts(String message) {
        Assert.assertEquals(message, getAllProductsAPIResponse.getMessage());
        Assert.assertEquals(getAllProductsAPIResponse.getData().size(), 0);
    }


}
