package HOMEWORK;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex12_WhatHeader {
    String url = "https://playground.learnqa.ru/api/homework_header";
    String cookie;
    Response response = RestAssured.get(url);

    @Test
    public void headerExists() {
        Response response = RestAssured.get(url);

    }


}
