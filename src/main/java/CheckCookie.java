import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class CookieLesson {

    @Test

    public void testRestAssured() {
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login2");
        data.put("password", "secret_pass2");
        // получим NULL если изменим на secret_login2 / secret_pass2

        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        System.out.println("\nPretty Text:");
        response.prettyPrint();

        System.out.println("\nHeaders:");
        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders);

        System.out.println("\nCookies:");
        Map<String, String> responseCookies = response.getCookies();
        System.out.println(responseCookies);


        /* 2 String responseCookie = response.getCookie("auth_cookie");
        System.out.println(responseCookie);

        2  */
    }
}