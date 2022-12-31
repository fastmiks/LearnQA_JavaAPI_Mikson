package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class UserEditTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test creates user, then does login action, edits name, and finally gets name from Response and compares it with")
    @DisplayName("Test edit user info, auth as same user")
    public void testEditJustCreatedTest() {
        // GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId = responseCreateAuth.getString("id"); // сохраняем id нового пользователя для дальнейших запросов

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //EDIT NAME
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET NAME AND COMPARE NAME
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        // System.out.println(responseUserData.asString());
        Assertions.assertJsonByName(responseUserData, "firstName", newName);
        
    }

    // Ex17: Негативные тесты на PUT
    // - Попытаемся изменить данные пользователя, будучи неавторизованными
    @Test
    @Description("Change user data without being authorized")
    @DisplayName("Test negative, edit user info, no auth")
    public void testEditUserNoAuth () {

        String newUsername = "newLearnQA";
        int randomId = (int)Math.floor(Math.random()*(50000-11+1)+11);

        Map<String, String> editData = new HashMap<>();
        editData.put("username", newUsername);

        Response responseEditUserWithNoAuth = apiCoreRequests
                .makePutRequestWithoutTokenAndCookie(
                        "https://playground.learnqa.ru/api/user/" + randomId, editData)
                ;

        // MIC-CHECK
        // System.out.println(responseEditUserWithNoAuth.getStatusCode());
        // System.out.println(responseEditUserWithNoAuth.asString());

        Assertions.assertResponseCodeEquals(responseEditUserWithNoAuth, 400);
        Assertions.assertResponseTextEquals(responseEditUserWithNoAuth, "Auth token not supplied");
    }

    // - Попытаемся изменить данные пользователя, будучи авторизованными другим пользователем
    @Test
    @Description("Change user data while being authorized as different user")
    @DisplayName("Test negative, edit user info, auth as different user")
    public void testEditUserDataAuthAsDiffUser() {
        // CREATE NEW USER
        Map<String, String> newUserData = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser = apiCoreRequests
                .makePostRequestToCreateUser(
                        "https://playground.learnqa.ru/api/user/",
                        newUserData
                );
        String userId = responseCreateUser.getString("id");

        // LOGIN AS NEW USER
        Response responseCreateAuthNewUser = apiCoreRequests
                .makePostRequestToAuthUser(
                        newUserData.get("email"),
                        newUserData.get("password")
                );

        //LOGIN AS KOT
        Response responseCreateAuthKot = apiCoreRequests
                .makePostRequestToAuthUser(
                        "vinkotov@example.com",
                        "1234"
                );

        // EDIT DATA
        String newName = "Alexey";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        // TRYING TO CHANGE NAME OF NEW USER WITH KOT
        Response responseEditUser = apiCoreRequests
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        editData,
                        getHeader(responseCreateAuthKot, "x-csrf-token"),
                        getCookie(responseCreateAuthKot, "auth_sid")
                );

        Assertions.assertResponseCodeEquals(responseEditUser, 400);

        //CHECK NAME OF THE NEW USER
        Response responseNewUserData = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        getHeader(responseCreateAuthNewUser, "x-csrf-token"),
                        getCookie(responseCreateAuthNewUser, "auth_sid")
                );

        Assertions.assertJsonByName(responseNewUserData, "firstName", newUserData.get("firstName"));
    }

    @Test
    @Description("Change user email to email without @ symbol, while being authorized as the same user")
    @DisplayName("Test negative, change user email to email without @")
    public void testChangeUserEmailToInvalid() {
        //CREATE USER
        Map<String, String> newUserData = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser = apiCoreRequests
                .makePostRequestToCreateUser(
                        "https://playground.learnqa.ru/api/user/",
                        newUserData
                );
        String userId = responseCreateUser.getString("id");

        //LOGIN AS NEW USER
        Response responseCreateAuthNewUser = apiCoreRequests
                .makePostRequestToAuthUser(
                        newUserData.get("email"),
                        newUserData.get("password")
                );

        //EDIT DATA
        String invalidEmail = DataGenerator.getRandomEmail().replaceAll("@", "");
        Map<String, String> editData = new HashMap<>();
        editData.put("email", invalidEmail);

        Response responseEditNewUser = apiCoreRequests
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/",
                        editData,
                        getHeader(responseCreateAuthNewUser, "x-csrf-token"),
                        getCookie(responseCreateAuthNewUser, "auth_sid")
                );
        //CHECK
        Assertions.assertResponseCodeEquals(responseEditNewUser, 400);
        Assertions.assertResponseTextEquals(responseEditNewUser, "Invalid email format");

        //GET DATA TO CHECK
        Response responseUserData = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        getHeader(responseCreateAuthNewUser, "x-csrf-token"),
                        getCookie(responseCreateAuthNewUser, "auth_sid")
                        );

        //CHECK
        Assertions.assertJsonByName(responseUserData, "email", newUserData.get("email"));
    }

    @Test
    @Description("Change user first name to a name containing only one character")
    @DisplayName("Test negative, change user name to one char")
    public void testChangeFirstNameToOneChar() {
        //CREATE USER
        Map<String, String> newUserData = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser = apiCoreRequests
                .makePostRequestToCreateUser(
                        "https://playground.learnqa.ru/api/user/",
                        newUserData
                );
        String userId = responseCreateUser.getString("id");

        //LOGIN AS NEW USER
        Response responseCreateAuthNewUser = apiCoreRequests
                .makePostRequestToAuthUser(
                        newUserData.get("email"),
                        newUserData.get("password")
                );

        //EDIT NEW USER FIRST NAME AS SAME USER
        String firstNameTooShort = RandomStringUtils.randomAlphanumeric(1);;
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", firstNameTooShort);

        Response responseEditNewUser = apiCoreRequests
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/",
                        editData,
                        getHeader(responseCreateAuthNewUser, "x-csrf-token"),
                        getCookie(responseCreateAuthNewUser, "auth_sid")
                );
        //CHECK
        Assertions.assertResponseCodeEquals(responseEditNewUser, 400);
        Assertions.assertResponseTextEquals(responseEditNewUser, "{\"error\":\"Too short value for field firstName\"}");

        //GET DATA TO CHECK
        Response responseUserData = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        getHeader(responseCreateAuthNewUser, "x-csrf-token"),
                        getCookie(responseCreateAuthNewUser, "auth_sid")
                );

        //CHECK
        Assertions.assertJsonByName(responseUserData, "firstName", newUserData.get("firstName"));
    }

}
