package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    public static void assertJsonHasKey (Response Response, String expectedFieldName) {
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
     }
}
