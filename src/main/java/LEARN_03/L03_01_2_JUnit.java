package LEARN_03;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class L03_01_2_JUnit {

    @Test
    public void testPassed() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map") //
                .andReturn();
        assertEquals(200, response.statusCode(), "Unexpected status code");
    }

    @Test
    public void testFailed() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/map2") //
                .andReturn();
        assertEquals(200, response.statusCode(), "Unexpected status code");
    }
}
