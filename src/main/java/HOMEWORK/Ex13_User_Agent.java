package HOMEWORK;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex13_User_Agent {

    @ParameterizedTest
    @CsvFileSource(resources = {"/data.txt" })

    public void testUserAgentCheckRequest(String userAgent, String platformFromFile, String browserFromFile, String deviceFromFile) {

        JsonPath response = RestAssured
                .given()
                .header("user-agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();

        String platformFromResponse = response.get("platform");
        String browserFromResponse = response.get("browser");
        String deviceFromResponse = response.get("device");

        assertTrue(platformFromResponse.equals(platformFromFile), String.format("Expected platform: '%s'\n " + "Actual platform: '%s'", platformFromResponse, platformFromFile));
        assertTrue(browserFromResponse.equals(browserFromFile), String.format("Expected browser: '%s'\n " + "Actual browser: '%s'", browserFromResponse, browserFromFile));
        assertTrue(deviceFromResponse.equals(deviceFromFile), String.format("Expected device: '%s'\n " + "Actual device: '%s'", deviceFromResponse, deviceFromFile));

    }
}
