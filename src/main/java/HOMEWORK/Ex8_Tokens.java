/*Ex8: Токены

Иногда API-метод выполняет такую долгую задачу, что за один HTTP-запрос от него нельзя сразу получить готовый ответ.
Это может быть подсчет каких-то сложных вычислений или необходимость собрать информацию по разным источникам.

В этом случае на первый запрос API начинает выполнения задачи, а на последующие ЛИБО говорит, что задача еще не готова, ЛИБО выдает результат. Сегодня я предлагаю протестировать такой метод.

Сам API-метод находится по следующему URL: https://playground.learnqa.ru/ajax/api/longtime_job

Если мы вызываем его БЕЗ GET-параметра token, метод заводит новую задачу, а в ответ выдает нам JSON со следующими полями:
* seconds - количество секунд, через сколько задача будет выполнена
* token - тот самый токен, по которому можно получить результат выполнения нашей задачи

Если же вызвать API-метод, УКАЗАВ GET-параметром token, то мы получим следующий JSON:
* error - будет только в случае, если передать token, для которого не создавалась задача. В этом случае в ответе будет следующая надпись - No job linked to this token
* status - если задача еще не готова, будет надпись Job is NOT ready, если же готова - будет надпись Job is ready
* result - будет только в случае, если задача готова, это поле будет содержать результат

Наша задача - написать тест, который сделал бы следующее:

1) создавал задачу
2) делал один запрос с token ДО того, как задача готова, убеждался в правильности поля status
3) ждал нужное количество секунд с помощью функции time.sleep() - для этого надо сделать import time
4) делал бы один запрос c token ПОСЛЕ того, как задача готова, убеждался в правильности поля status и наличии поля result

Как всегда, код нашей программы выкладываем ссылкой на коммит.
 */
package HOMEWORK;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Ex8_Tokens {
    @Test
    public void myJob() throws InterruptedException {

/* Посмотрим ошибку
        Response checkNoTask = RestAssured
                .given()
                .queryParam("token")
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();

        checkNoTask.prettyPrint();
*/
        // 1) создавал задачу
        JsonPath createTask = RestAssured
                .given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String token = createTask.get("token");
        createTask.prettyPrint();
        System.out.println("\n");

        // 2) делал один запрос с token ДО того, как задача готова, убеждался в правильности поля status
        Map<String, String> myToken = new HashMap<>();
        myToken.put("token", token);

        JsonPath areYouReady = RestAssured
                .given()
                .queryParams(myToken)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        areYouReady.prettyPrint();
        System.out.println("\n");

        // 3) ждал нужное количество секунд с помощью функции time.sleep() - для этого надо сделать import time
        // time.sleep похоже в питоне?
        Thread.sleep(18000);

        // 4) делал бы один запрос c token ПОСЛЕ того, как задача готова
        JsonPath response = RestAssured
                .given()
                .queryParams(myToken)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        // убеждался в правильности поля status и наличии поля result
        if ((response.get("result") != null) && (response.get("status") != "Job is NOT ready")) {
            response.prettyPrint();
        } else {
            System.out.println("Not ready yet");
        }
    }
}
