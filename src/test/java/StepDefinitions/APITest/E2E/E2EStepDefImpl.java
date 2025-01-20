package StepDefinitions.APITest.E2E;

import BaseTest.BaseTest;
import POJO.Request.Order;
import POJO.Request.*;
import POJO.Response.*;
import com.github.javafaker.Faker;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;
import java.util.*;

import static io.restassured.RestAssured.given;

public class E2EStepDefImpl extends BaseTest {

    RequestSpecification request;
    LoginAPIResponse loginResponse;
    ForgotPasswordAPIResponse forgotPasswordAPIResponse;
    RegisterAPIResponse registerResponse;
    GetAllProductsAPIResponse getAllProductsAPIResponse;
    GetProductDetailsResponse getProductDetailsResponse;
    GetCartCountAPI_NoCartProductsResponse getCartCountAPINoCartProductsResponse;
    AddToCartAPI_SuccessResponse addToCartAPIResponse;
    GetCartCountAPI_CartProductsResponse getCartCountAPICartProductsResponse;
    GetCartProductsAPI_WithCartproducts getCartProductsAPICartProductsResponse;
    AddProductAPI_SuccessResponse addProductAPIResponse;
    CreateOrderAPI_SuccessResponse createOrderAPIResponse;
    GetOrdersAPI_WithOrders getOrdersAPIWithOrders;
    DeleteOrderAPI_SuccessResponse deleteOrderAPIResponse;
    DeleteProductAPI_SuccessResponse deleteProductAPIResponse;


    List<Product> products;
    List<Product> productsToAdd = new ArrayList<>();
    List<String> productIdsToAdd = new ArrayList<>();
    List<String> orderIds;
    int count = 3;
    String newProductId;

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
        int row = excelUtils.getRowCount();
        excelUtils.writeCell(row, 0, email);
        excelUtils.writeCell(row, 1, password);
        excelUtils.save("Data.xlsx");
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
        int row = excelUtils.getRowCount() - 1;
        excelUtils.writeCell(row, 0, email);
        excelUtils.writeCell(row, 1, updatedPassword);
        excelUtils.save("Data.xlsx");
        close();
    }

    @When("user sends login api request with valid details")
    public void userSendsLoginApiRequestWithValidDetails() {

        //Reading data from excel sheet for login request
        int row = excelUtils.getRowCount();
        List<Map<String, String>> data = excelUtils.readRows(row - 1, row - 1);

        //Creating login api request body
        LoginAPIRequest loginAPIRequest = new LoginAPIRequest();
        loginAPIRequest.setUserEmail(data.get(0).get("UserName"));
        loginAPIRequest.setUserPassword(data.get(0).get("Password"));

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
            if (!productIdsToAdd.contains(product.get_id())) {
                productsToAdd.add(product);
                productIdsToAdd.add(product.get_id());
                System.out.println(productsToAdd.size());
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

    @When("User sends get cart count api request with user id")
    public void userSendsGetCartCountApiRequestWithUserId() {
        //Sending get cart count api request and extracting response as GetCartCountAPI_NoCartProductsResponse object and validating response code and response time
        getCartCountAPINoCartProductsResponse = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .pathParam("userId", loginResponse.getUserId())
                .when()
                .get("user/get-cart-count/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetCartCountAPI_NoCartFoundSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetCartCountAPI_NoCartProductsResponse.class);
    }

    @Then("User should receive {string} message in get cart count api response")
    public void userShouldReceiveMessageInGetCartCountApiResponse(String message) {
        //Validating message
        Assert.assertEquals(message, getCartCountAPINoCartProductsResponse.getMessage());
    }

    @When("User sends add product to cart request with valid product id")
    public void userSendsAddProductToCartRequestWithValidProductId() {
        for (Product prod : productsToAdd) {
            //Creating add to cart request body
            AddToCartAPIRequest requestBody = new AddToCartAPIRequest();
            requestBody.setProduct(prod);
            requestBody.set_id(loginResponse.getUserId());

            //Sending add to cart api request body
            addToCartAPIResponse = given().spec(requestSpecification).body(requestBody)
                    .header("Authorization", loginResponse.getToken()).log().all().when()
                    .post("/user/add-to-cart")
                    .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/AddToCartAPI_SuccessSchema.json")))
                    .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                    .extract().as(AddToCartAPI_SuccessResponse.class);
        }
    }

    @Then("{string} message is returned in add to cart api response")
    public void messageIsReturnedInAddToCartApiResponse(String message) {
        //Validating message is returned in add to cart api response
        Assert.assertEquals(message, addToCartAPIResponse.getMessage());
    }

    @When("User sends get cart count api request with user id with products in cart")
    public void userSendsGetCartCountApiRequestWithUserIdWithProductsInCart() {
        //Sending get cart count api request with products in cart and extracting response as GetCartCountAPI_CartProductsResponse object and validating response code, response time, and product count
        getCartCountAPICartProductsResponse = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .pathParam("userId", loginResponse.getUserId())
                .when()
                .get("user/get-cart-count/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetCartCountAPI_CartFoundSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetCartCountAPI_CartProductsResponse.class);
    }

    @Then("User should receive product count in response")
    public void userShouldReceiveProductCountInResponse() {
        //Validating product count in get cart count api response with products in cart
        Assert.assertEquals(productsToAdd.size(), getCartCountAPICartProductsResponse.getCount());

    }

    @When("User ends Get cart product details api request")
    public void userEndsGetCartProductDetailsApiRequest() {
        //Sending getCartProducts api request
        getCartProductsAPICartProductsResponse = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .pathParam("userId", loginResponse.getUserId()).log().all().when()
                .get("/user/get-cart-products/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetCartProductsAPI_WithProductsSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetCartProductsAPI_WithCartproducts.class);
    }

    @Then("User should receive a list of product details get cart product details and {string} message in get cart details api response")
    public void userShouldReceiveAListOfProductDetailsGetCartProductDetailsAndMessageInGetCartDetailsApiResponse(String message) {
        //Validating message and product details in get cart product details api response
        Assert.assertEquals(message, getCartProductsAPICartProductsResponse.getMessage());
        Assert.assertFalse(getCartProductsAPICartProductsResponse.getProducts().isEmpty());

    }

    @When("user sends a request to add product with all valid details")
    public void userSendsARequestToAddProductWithAllValidDetails() {
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

    @Then("{string} message is returned along with product id in add product response")
    public void messageIsReturnedAlongWithProductIdInAddProductRsponse(String message) {
        //Validating message from response
        Assert.assertEquals(message, addProductAPIResponse.getMessage());
        //Validating product id in response
        Assert.assertNotNull(addProductAPIResponse.getProductId());
        newProductId = addProductAPIResponse.getProductId();
    }

    @When("User sends get product details request with new product id")
    public void userSendsGetProductDetailsRequestWithNewProductId() {
        //Sending get product details api request with new product id and extracting response as GetProductDetailsResponse object
        getProductDetailsResponse = given().spec(requestSpecification).pathParam("productId", newProductId)
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .get("/product/get-product-detail/{productId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetProductDetails_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(GetProductDetailsResponse.class);
    }

    @When("User sends add product to cart request with new product id")
    public void userSendsAddProductToCartRequestWithNewProductId() {
        //Creating add to cart request body
        AddToCartAPIRequest requestBody = new AddToCartAPIRequest();
        requestBody.setProduct(getProductDetailsResponse.getData());
        requestBody.set_id(loginResponse.getUserId());

        //Sending add to cart api request body
        addToCartAPIResponse = given().spec(requestSpecification).body(requestBody)
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .post("/user/add-to-cart")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/AddToCartAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(AddToCartAPI_SuccessResponse.class);
    }


    @When("User sends create order api request with product details from cart")
    public void userSendsCreateOrderApiRequestWithProductDetailsFromCart() {
        //Creating request body for create order api request
        List<Order> orderList = new ArrayList<>();
        for (Product product : getCartProductsAPICartProductsResponse.getProducts()) {
            Order order = new Order("India", product.get_id());
            orderList.add(order);
        }


        // Create OrdersRequest with a list of orders
        CreateOrderAPIRequest ordersRequest = new CreateOrderAPIRequest(orderList);
//        System.out.println(orders);
        createOrderAPIResponse = given().spec(requestSpecification).body(ordersRequest)
                .header("Authorization", loginResponse.getToken()).log().all().when()
                .post("order/create-order")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/CreateOrderAPI_SuccessShema.json")))
                .spec(getResponseSpecification(201, responseTime, ContentType.JSON)).log().all()
                .extract().as(CreateOrderAPI_SuccessResponse.class);
    }

    @Then("{string} message is sent to user along with order ids in create order api response")
    public void messageIsSentToUserAlongWithOrderIdsInCreateOrderApiResponse(String message) {
        //Verifying message in response
        Assert.assertEquals(createOrderAPIResponse.getMessage(), message);
        orderIds = createOrderAPIResponse.getOrders();
    }

    @When("User sends get orders api request with valid userID EtoE")
    public void userSendsGetOrdersApiRequestWithValidUserIDEtoE() {
        //Sending get orders api request
        getOrdersAPIWithOrders = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .pathParam("userId", loginResponse.getUserId())
                .log().all().when()
                .get("order/get-orders-for-customer/{userId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/GetOrdersAPI_WithOrders.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(GetOrdersAPI_WithOrders.class);
    }

    @Then("user should receive order details get orders and {string} message")
    public void userShouldReceiveOrderDetailsGetOrdersAndMessage(String message) {
        //Validating message in response
        Assert.assertEquals(getOrdersAPIWithOrders.getMessage(), message);
        //Validating order IDS
        for (POJO.Response.Order order : getOrdersAPIWithOrders.getData()) {
            Assert.assertTrue(orderIds.contains(order.get_id()));

        }
    }


    @When("User sends valid order id to delete in delete order api")
    public void userSendsValidOrderIdToDeleteInDeleteOrderApi() {
        //Sending delete order request with valid order id
        deleteOrderAPIResponse = given().spec(requestSpecification)
                .header("Authorization", loginResponse.getToken())
                .pathParam("orderId", orderIds.get(0))
                .log().all().when()
                .delete("order/delete-order/{orderId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/DeleteOrderAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract().as(DeleteOrderAPI_SuccessResponse.class);
    }

    @Then("User receives {string} message in delete order api response EtoE")
    public void userReceivesMessageInDeleteOrderApiResponseEtoE(String message) {
        //Verifying message in response
        Assert.assertEquals(deleteOrderAPIResponse.getMessage(), message);
    }

    @When("User sends valid product id to delete product api")
    public void userSendsValidProductIdToDeleteProductApi() {
        //Deleting product
        deleteProductAPIResponse = given().spec(requestSpecification).header("Authorization", loginResponse.getToken())
                .pathParam("productId", newProductId).log().all().when()
                .delete("product/delete-product/{productId}")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/schemas/DeleteProductAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(DeleteProductAPI_SuccessResponse.class);
    }

    @Then("User receives {string} message in delete product api response EtoE")
    public void userReceivesMessageInDeleteProductApiResponseEtoE(String message) {
        //Validating message in response
        Assert.assertEquals(deleteProductAPIResponse.getMessage(), message);
    }
}
