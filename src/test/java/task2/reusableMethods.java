package task2;

import com.aventstack.extentreports.ExtentTest;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static org.hamcrest.MatcherAssert.assertThat;

public class reusableMethods {
    public static JsonPath rawToJson(String response) {
        return new JsonPath(response);
    }

    public static void validateAssertion(ExtentTest test, Object actual, Object expected, String fieldName) {
        try {
            Assert.assertEquals(actual, expected, fieldName + " - Expected: [" + expected + "], Found: [" + actual + "]");
            test.pass(fieldName + " is " + actual + " and it matches.");
        } catch (AssertionError e) {
            test.fail(fieldName + " is " + actual + " and it does not match. " + e.getMessage());
        }
    }
}
