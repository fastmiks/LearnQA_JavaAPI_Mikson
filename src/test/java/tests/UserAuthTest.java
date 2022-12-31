package tests;

import io.qameta.allure.*;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import lib.BaseTestCase;
import lib.Assertions; // добавили
import lib.ApiCoreRequests; // добавили в 4-5

@Owner("Aleksejs Miksons")
@Epic("User dashboard basic functionality testing")
@Feature("Authorization")
public class UserAuthTest extends BaseTestCase { // с исправлениями с лекции 3-7

    String cookie;
    String header;
    int userIdOnAuth;

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeEach
    public void loginUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        // добавили улучшенный запрос в 4-5, прописав сам запрос в классе apiCoreRequests
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        /* благодаря классу apiCoreRequests, упраздняется запись запросов
                Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
         */


        this.cookie = this.getCookie(responseGetAuth, "auth_sid"); // обращаемся к функциям в библиотеке BaseTestCase
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");
    }

    @Test
    @Description("This test successfully authorize user by email and password")
    @DisplayName("Test positive auth user")
    @Severity(value = SeverityLevel.BLOCKER)
    @TmsLink("TL-043")
    public void testAuthUser() {

        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/auth",
                        this.header,
                        this.cookie
                );

        /* добавили улучшенный запрос в 4-5, прописав сам запрос в классе apiCoreRequests
        Response responseCheckAuth = RestAssured // JsonPath => Response
                .given()
                .header("x-csrf-token", this.header)
                .cookie("auth_sid", this.cookie)
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn(); */

        // удалили сравнения, что писали и используем функцию с библиотеки Assertions
        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }

    @Description("This test checks authorization status w/o sending auth cookie or token")
    @DisplayName("Test negative auth user")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLink("TL-044")
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})

    public void testNegativeAuthUser(String condition) {

        if (condition.equals("cookie")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.cookie
            );
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else if (condition.equals("headers")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.header
            );
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else {
            throw new IllegalArgumentException("Condition value is not known: " + condition);
        }


        /* упразднили в 4-5 и написали новый код выше
        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        // здесь и пишем негативный тест, где передаем либо куки, либо хедер
        if (condition.equals("cookie")){
            spec.cookie("auth_sid", this.cookie);
        } else if (condition.equals("headers")){
            spec.headers("x-csrf-token", this.header);
        } else {
            throw new IllegalArgumentException("Condition value is known: " + condition);
        }


        // парсим ответ в переменную как json
        Response responseForCheck = spec.get().andReturn(); //  JsonPath => Response

        // меняем на наш Assert с библиотеки
        Assertions.assertJsonByName(responseForCheck, "user_id", 0);

         */

        /* первые изменения до 4-5
        Мы сменили Jsonpath на Response, потому что мы теперь не работаем с JSON напрямую
        - эта работа делается в библиотеке нашими функциями
         */
    }
}
