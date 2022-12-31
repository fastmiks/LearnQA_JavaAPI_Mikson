package tests;

import io.qameta.allure.*;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;

import java.util.Map;

@Owner("Aleksejs Miksons")
@Epic("User dashboard basic functionality testing")
@Feature("Delete user data")
public class UserDeleteTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String userUrl = "https://playground.learnqa.ru/api/user/";

    @Test
    @Description("Check that you cannot delete user with id #2")
    @DisplayName("Test negative, delete user id 2")
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("TL-53")
    public void testDeleteUserId2 () {
        //LOGIN AS KOT
        Response responseCreateAuthKot = apiCoreRequests
                .makePostRequestToAuthUser(
                        "vinkotov@example.com",
                        "1234"
                );

        String token = getHeader(responseCreateAuthKot, "x-csrf-token");
        String cookie = getCookie(responseCreateAuthKot, "auth_sid");

        //DELETE KOT AS KOT
        Response responseDeleteYourself = apiCoreRequests
                .makeDeleteRequest(
                        this.userUrl,
                        token,
                        cookie
                );

        //CHECK
        Assertions.assertResponseCodeEquals(responseDeleteYourself, 400);
        Assertions.assertResponseTextEquals(responseDeleteYourself, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

        //GET DATA TO CHECK
        Response responseUserData = apiCoreRequests
                .makeGetRequest(
                        this.userUrl + 2,
                        token,
                        cookie
                );

        // CHECK
        String[] expectedFieldNames = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFieldNames);
    }

    @Test
    @Description("Check if you can generate new user and then delete it")
    @DisplayName("Test positive, create and delete user")
    @Severity(SeverityLevel.MINOR)
    @TmsLink("TL-53")
    public void testCreateNewUserThenDeleteThisUser () {
        //CREATE NEW USER
        Map<String, String> newUserData = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser = apiCoreRequests
                .makePostRequestToCreateUser(
                        this.userUrl,
                        newUserData
                );
        String userId = responseCreateUser.getString("id");

        //LOGIN AS NEW USER
        Response responseCreateAuthNewUser = apiCoreRequests
                .makePostRequestToAuthUser(
                        newUserData.get("email"),
                        newUserData.get("password")
                );

        String token = getHeader(responseCreateAuthNewUser, "x-csrf-token");
        String cookie = getCookie(responseCreateAuthNewUser, "auth_sid");

        //CHECK LOGIN SUCCESS
        Assertions.assertResponseCodeEquals(responseCreateAuthNewUser, 200);

        //DELETE NEW USER
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest(
                        this.userUrl + userId,
                        token,
                        cookie
                );

        //GET DATA TO CHECK
        Response responseCheckDataById = apiCoreRequests
                .makeGetRequest(
                        userUrl + userId,
                        token,
                        cookie
                );

        //CHECK DELETE SUCCESSFUL
        Assertions.assertResponseTextEquals(responseCheckDataById, "User not found");
    }

    @Test
    @Description("Check if you can generate new user and then delete it with another authorized user")
    @DisplayName("Test negative, create user and delete it with another")
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("TL-53")
    public void testCreateNewUserThenDeleteItWithAnotherUser () {
        //CREATE NEW USER ONE
        Map<String, String> dataUserOne = DataGenerator.getRegistrationData();
        JsonPath responseCreateUserOne = apiCoreRequests
                .makePostRequestToCreateUser(
                        this.userUrl,
                        dataUserOne
                );
        String userOneId = responseCreateUserOne.getString("id");

        //CREATE NEW USER TWO
        Map<String, String> dataUSerTwo = DataGenerator.getRegistrationData();
        JsonPath responseCreateUserTwo = apiCoreRequests
                .makePostRequestToCreateUser(
                        this.userUrl,
                        dataUSerTwo
                );

        //LOGIN NEW USER TWO
        Response responseCreateAuth2= apiCoreRequests
                .makePostRequestToAuthUser(
                        dataUSerTwo.get("email"),
                        dataUSerTwo.get("password")
                );

        String tokenUserTwo = getHeader(responseCreateAuth2, "x-csrf-token");
        String cookieUserTwo = getCookie(responseCreateAuth2, "auth_sid");

        //CHECK LOGIN
        Assertions.assertResponseCodeEquals(responseCreateAuth2, 200);

        //DELETE NEW USER
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest(
                        this.userUrl + userOneId,
                        tokenUserTwo,
                        cookieUserTwo
                );

        //CHECK DELETE
        if (responseDeleteUser.prettyPrint().toString().length() > 0) {
            System.out.println("Response answer: ");
            responseDeleteUser.prettyPrint();

            //GET DATA TO CHECK
            Response responseCheckDataById = apiCoreRequests
                    .makeGetRequestWithoutTokenAndCookie(
                            userUrl,
                            userOneId
                    );

            //CHECK IF USER WAS NOT DELETED
            Assertions.assertResponseCodeEquals(responseCheckDataById, 200);
            Assertions.assertJsonHasField(responseCheckDataById, "username");
        } else {
            System.out.println("Response has no message from server, check status code");
            Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        }

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);

    }

}
