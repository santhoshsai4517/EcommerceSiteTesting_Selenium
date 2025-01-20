package StepDefinitions.APITest.ForgotPasswordAPI;


import BaseTest.BaseTest;
import POJO.Request.ForgotPasswordAPIRequest;
import POJO.Response.ForgotPasswordAPIResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class FunctionalityStepDefImpl extends BaseTest {

    RequestSpecification request;
    ForgotPasswordAPIResponse forgotPasswordAPIResponse;
    String updatePassword = "123";
    String email = "";

    @Given("User provides correct details")
    public void userProvidesCorrectDetails() {

        int row = excelUtils.getRowCount();
        List<Map<String, String>> data = excelUtils.readRows(row - 1, row - 1);

        email = data.get(0).get("UserName");

        ForgotPasswordAPIRequest forgotPasswordAPIRequest = new ForgotPasswordAPIRequest();
        forgotPasswordAPIRequest.setUserEmail(email);
        forgotPasswordAPIRequest.setUserPassword(updatePassword);
        forgotPasswordAPIRequest.setConfirmPassword(updatePassword);

        request = given().spec(requestSpecification).body(forgotPasswordAPIRequest).log().all();

    }

    @When("User sends forgot password request")
    public void userSendsForgotPasswordRequest() {
        forgotPasswordAPIResponse = request.when()
                .post("/auth/new-password")
                .then().body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/Schemas/ForgotPasswordAPI_SuccessSchema.json")))
                .spec(getResponseSpecification(200, responseTime, ContentType.JSON)).log().all()
                .extract()
                .as(ForgotPasswordAPIResponse.class);
    }

    @Then("{string} message is returned to user")
    public void messageIsReturnedToUser(String message) {
        Assert.assertEquals(message, forgotPasswordAPIResponse.getMessage());
        int row = excelUtils.getRowCount() - 1;
        excelUtils.writeCell(row, 0, email);
        excelUtils.writeCell(row, 1, updatePassword);
        excelUtils.save("Data.xlsx");
        close();
    }


}
