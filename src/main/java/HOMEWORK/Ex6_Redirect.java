/*Ex6: Редирект

Необходимо написать тест, который создает GET-запрос на адрес: https://playground.learnqa.ru/api/long_redirect

С этого адреса должен происходит редирект на другой адрес. Наша задача — распечатать адрес, на который редиректит указанные URL.

Ответом должна быть ссылка на тест в вашем репозитории.
 */
package HOMEWORK;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex6_Redirect {

    @Test
    public void getAddress() {

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String address = response.getHeader("Location");
        System.out.println(address);

    }
}
