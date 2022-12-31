package lib;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Assertions {

    /* на вход эта функция будет получать объект с ответом сервера,
    - чтобы вытянуть из него текст
    - также имя, по которому нужно искать значение JSON
    - и ожидаемое значение
     */
     public static void assertJsonByName(Response Response, String name, int expectedValue) {
         Response.then().assertThat().body("$", hasKey(name));

         int value = Response.jsonPath().getInt(name);
         assertEquals(expectedValue, value, "JSON value is not equal expected value"); // вместо возвращения значения, мы сравниваем полученное с ожидаемым
     }

    public static void assertJsonByName(Response Response, String name, String expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value is not equal expected value"); // вместо возвращения значения, мы сравниваем полученное с ожидаемым
    }

    public static void assertJsonNotEqualsByName(Response Response, String name, String expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertNotEquals(expectedValue, value, "JSON value is equal to expected value"); // вместо возвращения значения, мы сравниваем полученное с ожидаемым
    }

     public static void assertResponseTextEquals(Response Response, String expectedAnswer) {
         assertEquals(
                 expectedAnswer,
                 Response.asString(),
                 "Response text is not as expected"
         );
     }

    public static void assertResponseCodeEquals(Response Response, int expectedStatusCode) {
        assertEquals(
                expectedStatusCode,
                Response.getStatusCode(),
                "Response status code is not as expected"
        );
    }
    /* до улучшения кода
        public static void assertJsonHasKey (Response Response, String expectedFieldName) {
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
     }

     public static void assertJsonHasNotKey (Response Response, String unexpectedFieldName) {
         Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
     }
     */

    public static void assertJsonHasField(Response Response, String expectedFieldName) {
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
     }

    public static void assertJsonHasFields(Response Response, String [] expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
            Assertions.assertJsonHasField(Response, expectedFieldName);
        }
    }

     public static void assertJsonHasNotField(Response Response, String unexpectedFieldName) {
         Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
     }

    public static void assertJsonHasNotFields(Response response, String[] unexpectedFieldNames) {
        for (String unexpectedFieldName : unexpectedFieldNames) {
            Assertions.assertJsonHasNotField(response, unexpectedFieldName);
        }
    }

}
