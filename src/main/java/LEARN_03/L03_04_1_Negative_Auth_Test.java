package LEARN_03;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class L03_04_1_Negative_Auth_Test {

    @Test
    public void testAuthUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        // растащили responseGetAuth на три переменные ниже
        Map<String, String> cookies = responseGetAuth.getCookies();
        Headers headers = responseGetAuth.getHeaders();
        int userIdOnAuth = responseGetAuth.jsonPath().getInt("user_id");

        assertEquals(200, responseGetAuth.statusCode(), "Unexpected status code");
        assertTrue(cookies.containsKey("auth_sid"), "Response does not have 'auth_sid' cookie");
        assertTrue(headers.hasHeaderWithName("x-csrf-token"), "Response does not have 'x-csrf-token' cookie");
        assertTrue(responseGetAuth.jsonPath().getInt("user_id") > 0, "User ID should be greater than 0");

        JsonPath responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", responseGetAuth.getHeader("x-csrf-token"))
                .cookie("auth_sid", responseGetAuth.getCookie("auth_sid"))
                .get("https://playground.learnqa.ru/api/user/auth")
                .jsonPath();

        int userIdOnCheck = responseCheckAuth.getInt("user_id");
        assertTrue(userIdOnCheck > 0, "Unexpected user id" + userIdOnCheck);

        assertEquals(
                userIdOnAuth,
                userIdOnCheck,
                "user id from auth request is not equal user id from check request");

        }

    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})

    public void testNegativeAuthUser(String condition) {
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        Map<String, String> cookies = responseGetAuth.getCookies();
        Headers headers = responseGetAuth.getHeaders();

        // по сути разбиваем Builder на части с помощью spec
        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        // здесь и пишем негативный тест, где передаем либо куки, либо хедер
        if (condition.equals("cookie")){
            spec.cookie("auth_sid", cookies.get("auth_sid"));
        } else if (condition.equals("headers")){
            spec.headers("x-csrf-token", headers.get("x-csrf-token"));
        } else {
            throw new IllegalArgumentException("Condition value is known: " + condition);
        }

        // парсим ответ в переменную как json
        JsonPath responseForCheck = spec.get().jsonPath(); // так как мы URL уже задали выше в коде, здесь в get - не указываем

        // убеждаемся, что получим user_id = 0
        assertEquals(0, responseForCheck.getInt("user_id"), "user id should be 0 for unauth request");

    }
}
