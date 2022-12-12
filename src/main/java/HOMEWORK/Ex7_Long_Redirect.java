/*Ex7: Долгий редирект

Необходимо написать тест, который создает GET-запрос на адрес из предыдущего задания: https://playground.learnqa.ru/api/long_redirect

На самом деле этот URL ведет на другой, который мы должны были узнать на предыдущем занятии.
Но этот другой URL тоже куда-то редиректит. И так далее. Мы не знаем заранее количество всех редиректов и итоговый адрес.

Наша задача — написать цикл, которая будет создавать запросы в цикле, каждый раз читая URL для редиректа из нужного заголовка. И так, пока мы не дойдем до ответа с кодом 200.

Ответом должна быть ссылка на тест в вашем репозитории и количество редиректов.
 */
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
