package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String,String> userData = new HashMap<>();
        userData.put("email", email); // этот параметр взят со строки выше, остальные параметры сгенерированы
        userData = DataGenerator.getRegistrationData(userData);

        /* упраздняется после лекции 4-4, где мы создали метод генерации данных в классе DataGenerator
        userData.put("password", "123");
        userData.put("username", "leanqa");
        userData.put("firstName", "leanqa");
        userData.put("lastName", "leanqa");
        */

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        System.out.println(responseCreateAuth.asString() + "\n" + responseCreateAuth.getStatusCode());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");

    }

    @Test
    public void testCreateUserSuccessfully() {
        String email = DataGenerator.getRandomEmail();

        Map<String,String> userData = DataGenerator.getRegistrationData();

        /* упраздняется после лекции 4-4, где мы создали метод генерации данных в классе DataGenerator
        Map<String,String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123");
        userData.put("username", "leanqa");
        userData.put("firstName", "leanqa");
        userData.put("lastName", "leanqa");
         */

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        // заменили это на строчку ниже, после создания метода в Assertions | System.out.println(responseCreateAuth.asString());
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }
}
