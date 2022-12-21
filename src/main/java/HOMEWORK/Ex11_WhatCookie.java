/* Ex11: Тест запроса на метод cookie

Необходимо написать тест, который делает запрос на метод: https://playground.learnqa.ru/api/homework_cookie

Этот метод возвращает какую-то cookie с каким-то значением.
Необходимо понять что за cookie и с каким значением, и зафиксировать это поведение с помощью assert.
 */
package HOMEWORK;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class Ex11_WhatCookie {

    String url = "https://playground.learnqa.ru/api/homework_cookie";
    String cookie;
    Response response = RestAssured.get(url);


    @Test
    public void cookieExists() {

        assertFalse(this.response.getCookies().isEmpty(), "There are no cookies in here");
    }

    @Test
    public void cookieHasValue() {

        Map<String, String> cookiesFromURL = this.response.getCookies();
        String cookieName = cookiesFromURL.keySet().toString();
        String cookieValue = cookiesFromURL.values().toString();

        String expectedValue = (cookieValue.length() > 0) ? cookieValue : "";

        assertEquals(expectedValue, cookieValue, "Cookie has no value");

        System.out.println(
                "Cookie with name: "
                        + cookieName.replaceAll("[\\[\\]]", "")
                        + " has value: "
                        + cookieValue.replaceAll("[\\[\\]]", "")
        );
    }

    @Test
    public void cookieValueIsGood() {

        String cookieValue = response.getCookie("Homework");
        assertEquals("hw_value", cookieValue.replaceAll("[\\[\\]]", ""), "cookie value is not like in homework");
    }
}
