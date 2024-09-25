package task2;

public class Payload {
    public static String addItem() {
        return "{\n" +
                "    \"firstname\": \"testFirstName\",\n" +
                "    \"lastname\": \"lastName\",\n" +
                "    \"totalprice\": 10.11,\n" +
                "    \"depositpaid\": true,\n" +
                "    \"bookingdates\": {\n" +
                "        \"checkin\": \"2022-01-01\",\n" +
                "        \"checkout\": \"2024-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\": \"testAdd\"\n" +
                "}";
    }

    public static String missingFirstName() {
        return "{\n" +
                "    \"lastname\": \"lastName\",\n" +
                "    \"totalprice\": 10.11,\n" +
                "    \"depositpaid\": true,\n" +
                "    \"bookingdates\": {\n" +
                "        \"checkin\": \"2022-01-01\",\n" +
                "        \"checkout\": \"2024-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\": \"testAdd\"\n" +
                "}";
    }
}
