package LEARN_03;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class L03_02_2_Parameterized_test {

    @ParameterizedTest //добавили новую библиотеку: это тест с параметрами, будет запускаться столько тестов, сколько есть параметров
    @ValueSource(strings = {"", "John", "Pete"}) // отсюда будем брать эти самые параметры для теста

    public void testHelloMethodWithoutName(String name) { //Именно сюда будут передаваться наши параметры
        Map<String, String> queryParams = new HashMap<>(); // создаем пустой HashMap, в котором будут храниться параметры

        if (name.length() > 0) {
            queryParams.put("name", name); // добавляем имя в качестве параметра
        }

        JsonPath response = RestAssured
                .given()
                .queryParams(queryParams)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();

        String answer = response.getString("answer");
        String expectedName = (name.length() > 0) ? name: "someone2"; // собираем строку с ожидаемым результатом (это IF только в другом виде - тернарный оператор)
        assertEquals("Hello, " + expectedName, answer, "The answer is not expected");

    }
}
