package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

// пишем 2 метода: getHeader / getCookie
public class BaseTestCase {
    protected String getHeader(Response Response, String name) {
        Headers headers = Response.getHeaders(); // передаем headers
        // убеждаемся, что в соотв поля пришли значения
        assertTrue(headers.hasHeaderWithName(name), "Response does not have headers with name: " + name);
        return headers.getValue(name); // возвращаем значение
    }

    protected String getCookie(Response Response, String name) {
        Map<String, String> cookies = Response.getCookies(); // передаем cookie
        // убеждаемся, что в соотв поля пришли значения
        assertTrue(cookies.containsKey(name), "Response does not have cookie with name: " + name);
        return  cookies.get(name); // возвращаем значение
    }

    protected int getIntFromJson(Response Response, String name) {
        Response.then().assertThat().body("$",hasKey(name)); // $ указывает, что мы ищем наш ключ в корне JSON
        // если бы ключ был бы спрятан за вложенностями, то мы бы указывали путь до нужного поля
        return Response.jsonPath().getInt(name);
    }

}