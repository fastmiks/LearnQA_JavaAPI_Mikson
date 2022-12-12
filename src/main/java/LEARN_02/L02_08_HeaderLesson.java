package LEARN_02;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class L02_08_HeaderLesson {

    @Test

    public void testRestAssured() {
        Map<String, String> headers = new HashMap<>();
        headers.put("MyHeader1", "MyValue1");
        headers.put("MyHeader2", "MyValue2");

        Response response = RestAssured
                .given()
       // 1        .headers(headers)
                .redirects() // 2
                .follow( false) // 2
                .when()
      // 1 получаем все заголовки          .get("https://playground.learnqa.ru/api/show_all_headers")
                .get("https://playground.learnqa.ru/api/get_303") //2
                .andReturn();

        response.prettyPrint();

      // 1 Headers responseHeaders = response.getHeaders();
        String locationHeader = response.getHeader("Location"); // 2
      // 1 System.out.println(responseHeaders);
        System.out.println(locationHeader); // 2

    }
}