package LEARN_03;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class L03_02_1_Parameterized_test {
    @Test
    public void testHelloMethodWithoutName() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String answer = response.getString("answer");
        assertEquals("Hello, someone", answer, "The answer is not expected");
    }

    @Test
    public void testHelloMethodWithName() {
        String name = "Aleksejs";
        JsonPath response = RestAssured
                .given()
                .queryParam("name", name)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String answer = response.getString("answer");
        assertEquals("Hello, " + name, answer, "The answer is not expected");
    }
}
