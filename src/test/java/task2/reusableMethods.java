package task2;

import com.aventstack.extentreports.ExtentTest;
import io.restassured.path.json.JsonPath;
import org.hamcrest.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;

public class reusableMethods {
    public static JsonPath rawToJson(String response) {
        return new JsonPath(response);
    }

    public static void validateAssertion(ExtentTest test, Object actual, Matcher<Object> matcher, String message) {
        try {
            assertThat(message, actual, matcher);
            test.pass(message + " - validation successful.");
        } catch (AssertionError e) {
            test.fail(message + " - validation failed: " + e.getMessage());
        }
    }
}
