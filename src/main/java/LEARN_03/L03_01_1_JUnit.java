package LEARN_03;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class L03_01_1_JUnit {

    @Test
    public void test() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map") // change to bad URL to see error in assertTrue
                .andReturn();
        assertTrue(response.statusCode() == 200, "Unexpected status code");
    }
}
