package tests;

import io.qameta.allure.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;

import java.util.HashMap;
import java.util.Map;

@Owner("Aleksejs Miksons")
@Epic("User dashboard basic functionality testing")
@Feature("Reading user data")
public class UserGetTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    // Просмотр пользователя БЕЗ авторизации - мы получили только username
    @Test
    @Description("This test gets user information without auth, the result should be just JSON with username")
    @DisplayName("Test get user info, no auth")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("TL-045")
    public void testGetUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        // System.out.println(responseUserData.asString());
        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }
    //  Авторизованный запрос - мы были авторизованы пользователем с ID 2 и делали запрос для получения данных того же пользователя, в этом случае мы получали все поля
    @Test
    @Description("This test gets user information with auth as same user, the result should be JSON with all user data")
    @DisplayName("Test get user info, auth as same user")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("TL-046")
    public void testGetUserDetailsAuthAsSameUser () {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        String header = this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        // System.out.println(responseUserData.asString());

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);

        /* уже не нужны после создания отдельной функции в Assertions
        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasField(responseUserData, "firstName");
        Assertions.assertJsonHasField(responseUserData, "lastName");
        Assertions.assertJsonHasField(responseUserData, "email");
         */
    }

    // Ex16: Запрос данных другого пользователя
    // Тест, который авторизовался одним пользователем, но получает данные другого (т.е. с другим ID).
    // И убедиться, что в этом случае запрос также получает только username, так как мы не должны видеть остальные данные чужого пользователя.

    @Test
    @Description("This test gets user information with auth as different, the result should be JSON with username only")
    @DisplayName("Test get user info, auth as different user")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("TL-045")
    public void testGetUserDetailsAuthAsDifferentUser () {
        // TO MAKE TEST UNIVERSAL: CREATE NEW USER AND LOGIN
        Map<String, String> generatedUserData = DataGenerator.getRegistrationData();

        Response responseCreateAuthWithGenUser = apiCoreRequests
                .makePostRequest(
                        "https://playground.learnqa.ru/api/user",
                        generatedUserData
                );

        Response responseGetUserId = RestAssured
                .get("https://playground.learnqa.ru/api/user/auth")
                .andReturn();

        int createdUserId = this.getIntFromJson(responseGetUserId, "user_id");
        // System.out.println("user id: " + createdUserId);

        Response responseLoginWithGenUser = apiCoreRequests
                .makePostRequest(
                        "https://playground.learnqa.ru/api/user/login",
                        generatedUserData
                );

        String header = this.getHeader(responseLoginWithGenUser, "x-csrf-token");
        String cookie = this.getCookie(responseLoginWithGenUser, "auth_sid");
        int random_int = (int)Math.floor(Math.random()*(50000-11+1)+11);

        // NEW LOGGED IN USER GETS DATA FROM RANDOM USER WITH ID FROM 11 TO 50000
        Response responseGetAnotherUserData = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/" + random_int,
                        header,
                        cookie
                );
        /*
        Response responseGetUserId2Data = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/" + random_int)
                .andReturn(); */

        if (createdUserId != random_int) {
            System.out.println("user_id: " + random_int + " " + responseGetAnotherUserData.asString());
            String[] unexpectedFieldNames = {"firstName", "lastName", "email"};
            Assertions.assertJsonHasField(responseGetAnotherUserData, "username");
            Assertions.assertJsonHasNotFields(responseGetAnotherUserData, unexpectedFieldNames);
        } else {
            System.out.println("Lucky you, got the same user id as random, run the test again");
        }
    }

}
