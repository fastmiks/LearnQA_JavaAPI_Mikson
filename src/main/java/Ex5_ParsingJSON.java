/*
Ex5: Парсинг JSON

В рамках этой задачи нужно создать тест, который будет делать GET-запрос на адрес https://playground.learnqa.ru/api/get_json_homework
Полученный JSON необходимо распечатать и изучить. Мы увидим, что это данные с сообщениями и временем, когда они были написаны. Наша задача вывести текст второго сообщения.

Ответом должна быть ссылка на тест в вашем репозитории.import io.restassured.RestAssured;
 */

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


// 1)	Получить JSON в читаемом формате
public class Ex5_ParsingJSON {

    @Test
    public void parsing() {

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        response.prettyPrint();

// 2)	Вывести текст второго сообщения

        String message = response.getJsonObject("messages[1].message"); // идем во второй объект messages, в ключ message и записываем строку в переменную
        System.out.println("\n" + message);

    }
}