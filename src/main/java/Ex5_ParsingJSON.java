/*
Ex5: Парсинг JSON

В рамках этой задачи нужно создать тест, который будет делать GET-запрос на адрес https://playground.learnqa.ru/api/get_json_homework
Полученный JSON необходимо распечатать и изучить. Мы увидим, что это данные с сообщениями и временем, когда они были написаны. Наша задача вывести текст второго сообщения.

Ответом должна быть ссылка на тест в вашем репозитории.import io.restassured.RestAssured;
 */

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;


// 1)	Получить JSON в читаемом формате
public class Ex5_ParsingJSON {

    @Test
    public void parsing() {

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

response.prettyPrint();

// 2)	Вывести текст второго сообщения

        ArrayList message = response.getJsonObject("messages"); // записываем в переменную объекты массива JSON"
        System.out.println("\n"message.get(1)); // выводим 2 объект в списке
    }
}