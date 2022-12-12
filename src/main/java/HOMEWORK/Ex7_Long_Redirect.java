package HOMEWORK;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex7_Long_Redirect {

    @Test
    public void cycleGetTo200() {

        int statusCode = 0;
        int countRedirect = 0;
        String address = "https://playground.learnqa.ru/api/long_redirect";

        while (statusCode != 200) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(address)
                    .andReturn();
            address = response.getHeader("location");
            statusCode = response.getStatusCode();

            if (address != null) {
                System.out.println(address); // ради интереса куда прыгает
                System.out.println(statusCode); // для спокойствия
            } else {
                statusCode = response.getStatusCode();
            }
        }
        System.out.println(address);
        System.out.println(statusCode);
    }
}
