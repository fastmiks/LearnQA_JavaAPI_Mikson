package HOMEWORK;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex7_Long_Redirect {

    @Test
    public void cycleGetTo200() {

        int code = 0;
        String address = "https://playground.learnqa.ru/api/long_redirect";

        while (code != 200) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(address)
                    .andReturn();
            address = response.getHeader("location");
            code = response.getStatusCode();

            if (address != null) {
                System.out.println(address); // ради интереса куда прыгает
                System.out.println(code);
            } else {
                code = response.getStatusCode();
                System.out.println("Destination reached:" + response.getHeader("X-Host"));
            }
        }
        System.out.println(code);
    }
}
