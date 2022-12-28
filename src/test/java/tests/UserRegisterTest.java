package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import lib.ApiCoreRequests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Epic("Registration cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test tries to create user with existing email")
    @DisplayName("Test negative create user")
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
    @Description("This test tries to create user with generated email")
    @DisplayName("Test positive create user")
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

    // Ex15: Тесты на метод user
    // - Создание пользователя с некорректным email - без символа @
    @Test
    @Description("This test tries to create user with generated email, but no @ symbol")
    @DisplayName("Test negative create user, invalid mail")
    public void testCreateUserWithInvalidEmail() {

        String email = DataGenerator.getRandomEmail().replace("@", "");

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateUser = apiCoreRequests
                .makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData
        );

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, "Invalid email format");
    }

    // Ex15: Тесты на метод user
    // - Создание пользователя без указания одного из полей - с помощью @ParameterizedTest необходимо проверить,
    // что отсутствие любого параметра не дает зарегистрировать пользователя
    @ParameterizedTest
    @Description("This test tries to create user with generated email, but missing one of each fields")
    @DisplayName("Test negative create user, missing field")
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    public void testCreateUserMissingRequiredFields(String field) {

        Map<String, String> userData = DataGenerator.getRegistrationData();
        userData.replace(field, null);

        Response responseCreateUser = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData
        );

        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser, String.format("The following required params are missed: %s", field));
    }

    // Ex15: Тесты на метод user
    // - Создание пользователя с очень коротким именем в один символ
    @Test
    @Description("This test tries to create user with generated email, but with first name field being too short")
    @DisplayName("Test negative create user, first name check")
    public void testCreateUserWithOneCharFirstName() {

        String firstNameTooShort = RandomStringUtils.randomAlphanumeric(1);

        Map<String, String> userData = new HashMap<>();
        userData.put("firstName", firstNameTooShort);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateUser = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData
        );
        // System.out.println("this is what you get: " + firstNameTooShort);
        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser,  "The value of 'firstName' field is too short");
    }

    // Ex15: Тесты на метод user
    // - Создание пользователя с очень длинным именем - длиннее 250 символов
    @Test
    @Description("This test tries to create user with generated email, but with username field being too long")
    @DisplayName("Test negative create user, username length check")
    public void testCreateUserWithInvalidUsername() {

        String usernameTooLong = RandomStringUtils.randomAlphanumeric(251);

        Map<String, String> userData = new HashMap<>();
        userData.put("username", usernameTooLong);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateUser = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData
        );
        // System.out.println("this is what you get: " + usernameTooLong);
        Assertions.assertResponseCodeEquals(responseCreateUser, 400);
        Assertions.assertResponseTextEquals(responseCreateUser,  "The value of 'username' field is too long");
    }

}
