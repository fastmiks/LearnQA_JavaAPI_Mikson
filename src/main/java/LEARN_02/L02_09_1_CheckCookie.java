package LEARN_02;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class L02_09_1_CheckCookie {

    @Test

    public void testRestAssured() {
        Map<String, String> data = new HashMap<>();  // положили данные в data
        data.put("login", "secret_login"); // поставь "secret_login2" и мы получим you are not authorized
        data.put("password", "secret_pass");

        Response responseForGet = RestAssured // шлем запрос и получаем куки, ложим ответ в переменную responseForGet
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = responseForGet.getCookie("auth_cookie"); // получаем куки с названием "auth_cookie" и кладем в responseCookie

        Map<String, String> cookies = new HashMap<>(); // создаем HashMap для куки авторизации
        if(responseCookie != null){
            cookies.put("auth_cookie", responseCookie);
        } else {

        }

        Response responseForCheck = RestAssured
                .given()
                .body(data)
                .cookies(cookies)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

        responseForCheck.print();


    }
}