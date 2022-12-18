package LEARN_03;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

// пишем 2 метода: getHeader / getCookie
public class L03_06_1_Base_case {
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
}

// Чтобы использовались эти функции, нужно чтобы наши тесты наследовались от этого класса