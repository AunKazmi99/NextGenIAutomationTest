package task2;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

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
        test = extent.createTest("APIAssignment Test");
        String response = null;
        try {
            RestAssured.baseURI = "https://api.restful-api.dev";

            response = given().header("Content-Type", "application/json")
                    .body(Payload.AddDevice())
                    .when().post("objects").then()
                    .assertThat().statusCode(200).extract().response().asString();
            test.pass("API call succeeded");
        } catch (AssertionError e) {
            test.fail("API call failed: " + e.getMessage());
            Assert.fail("API call failed: " + e.getMessage());
        }

        if (response != null) {
            JsonPath jsonPath = reusableMethods.rawToJson(response);
            String id = jsonPath.getString("id");
            String name = jsonPath.getString("name");
            String createdAt = jsonPath.getString("createdAt");
            int year = jsonPath.getInt("data.year");
            double price = jsonPath.getDouble("data.price");

            reusableMethods.validateAssertion(test, id, is(notNullValue()), "ID should not be null");
            reusableMethods.validateAssertion(test, createdAt, is(notNullValue()), "CreatedAt should not be null");

            reusableMethods.validateAssertion(test, name, is(equalTo("Apple Max Pro 1TB")), "Name should match");
            reusableMethods.validateAssertion(test, year, is(equalTo(2023)), "Year should match");
            reusableMethods.validateAssertion(test, price, is(equalTo(7999.99)), "Price should match");
        } else {
            test.fail("API call returned null or empty response");
            Assert.fail("API call returned null or empty response");
        }
    }

    @AfterClass
    public void tearDown() {
        extent.flush();
    }
}
