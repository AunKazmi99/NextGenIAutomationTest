package task2;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static io.restassured.RestAssured.given;

public class Task2Test {
    public ExtentReports extent;
    public ExtentTest test;

    @BeforeClass
    public void setup() {
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    @Test
    public void APIAssignment() {
        long startTime = System.currentTimeMillis();
        test = extent.createTest("APIAssignment Test", "These tests verifies the booking API functionality")
                .assignAuthor("Muhammad Aun Abbas Kazmi")
                .assignCategory("API Testing")
                .assignDevice("Windows 11");
        SoftAssert softAssert = new SoftAssert();
        String response = null;
        try {
            RestAssured.baseURI = "https://restful-booker.herokuapp.com";
//Post API
            test.info("Starting Add Item API call");
            test.info("Request:" + Payload.addItem());
            response = given().header("Content-Type", "application/json")
                    .body(Payload.addItem())
                    .when().post("booking").then()
                    .assertThat().statusCode(200).extract().response().asString();
            test.info("Response: " + response);
            test.pass("Add Item API call successful").assignCategory("Success");
        } catch (AssertionError e) {
            test.fail("Add Item API call failed: " + e.getMessage());
            softAssert.fail("Add Item API call failed: " + e.getMessage());
        }

        JsonPath jsonPath = reusableMethods.rawToJson(response);
        int bookingId = jsonPath.getInt("bookingid");
        String postApiFirstName = jsonPath.getString("booking.firstname");
        String postApiLastName = jsonPath.getString("booking.lastname");
        double postApiTotalPrice = jsonPath.getDouble("booking.totalprice");
        boolean postApiDepositPaid = jsonPath.getBoolean("booking.depositpaid");
        String postApiAdditionalNeeds = jsonPath.getString("booking.additionalneeds");
        String postApiCheckInDate = jsonPath.getString("booking.bookingdates.checkin");
        String postApiCheckOutDate = jsonPath.getString("booking.bookingdates.checkout");

        String response2 = null;
// Get API
        try {
            response2 = given().when().get("/booking/" + bookingId).then()
                    .assertThat().statusCode(200).extract().response().asString();
            test.info("Response: " + response2);
            test.pass("Get Item API call successful").assignCategory("Success");
        } catch (AssertionError e) {
            test.fail("Get Item API call failed: " + e.getMessage());
            softAssert.fail("Get Item API call failed: " + e.getMessage());
        }

        JsonPath jsonPath2 = reusableMethods.rawToJson(response2);
        String getApiFirstName = jsonPath2.getString("firstname");
        String getApiLastName = jsonPath2.getString("lastname");
        double getApiTotalPrice = jsonPath2.getDouble("totalprice");
        boolean getApiDepositPaid = jsonPath2.getBoolean("depositpaid");
        String getApiAdditionalNeeds = jsonPath2.getString("additionalneeds");
        String getApiCheckInDate = jsonPath2.getString("bookingdates.checkin");
        String getApiCheckOutDate = jsonPath2.getString("bookingdates.checkout");

        reusableMethods.validateAssertion(test, postApiFirstName, getApiFirstName, "First Name");
        reusableMethods.validateAssertion(test, postApiLastName, getApiLastName, "Last Name");
        reusableMethods.validateAssertion(test, postApiTotalPrice, getApiTotalPrice, "Total Price");
        reusableMethods.validateAssertion(test, postApiDepositPaid, getApiDepositPaid, "Deposit Paid");
        reusableMethods.validateAssertion(test, postApiAdditionalNeeds, getApiAdditionalNeeds, "Additional Needs");
        reusableMethods.validateAssertion(test, postApiCheckInDate, getApiCheckInDate, "Check-in Date");
        reusableMethods.validateAssertion(test, postApiCheckOutDate, getApiCheckOutDate, "Check-out Date");

//Negative Case (Missing Data)
        test.info("1st Negative Case -> Bad Request");
        test.info("Request:" + Payload.missingFirstName());
        try {
            given().header("Content-Type", "application/json")
                    .body(Payload.missingFirstName())
                    .when().post("booking").then()
                    .assertThat().statusCode(500);
            test.pass("API Status Code is correct in case of Missing Data (Although it should be 400 but it is not correctly developed)")
                    .assignCategory("Success");
        } catch (AssertionError e) {
            test.fail("API Status Code is incorrect in case of Missing Data: " + e.getMessage());
            softAssert.fail("API Status Code is incorrect in case of Missing Data: " + e.getMessage());
        }

// Negative Case (Invalid Method -> PUT)
        test.info("2nd Negative Case -> Invalid HTTP Method");
        try {
            given().header("Content-Type", "application/json")
                    .body(Payload.missingFirstName())
                    .when().put("booking").then()
                    .assertThat().statusCode(404);
            test.pass("API Status Code is correct in case of Invalid Method (Although it should be 405 but is not correctly developed)")
                    .assignCategory("Success");
        } catch (AssertionError e) {
            test.fail("API Status Code is incorrect in case of Invalid Method: " + e.getMessage());
            softAssert.fail("API Status Code is incorrect in case of Invalid Method: " + e.getMessage());
        }

        softAssert.assertAll();
        long endTime = System.currentTimeMillis();
        test.info("Execution Time: " + (endTime - startTime) / 1000 + " seconds");
    }

    @AfterClass
    public void tearDown() {
        extent.flush();
    }
}
