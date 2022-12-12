package LEARN_02;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class L02_07_ResponseCodes {

    @Test

    public void testRestAssured() {
        Response response = RestAssured
        // первый 200       .get("https://playground.learnqa.ru/api/check_type")
        // второй 500       .get("https://playground.learnqa.ru/api/get_500")
        // третий 404       .get("https://playground.learnqa.ru/api/something")
        /* четвертый 303 */
                            .given()
                            .redirects()
                            .follow(true) // false выдаст 200
                            .when()
                            .get("https://playground.learnqa.ru/api/get_303")
                            .andReturn();

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
    }
}