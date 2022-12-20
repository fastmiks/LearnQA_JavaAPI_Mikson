package HOMEWORK;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class Ex12_WhatHeader {
    String url = "https://playground.learnqa.ru/api/homework_header";
    String cookie;
    Response response = RestAssured.get(url);

    @Test
    public void checkHeaders() {

        List<Header> headerList = this.response.getHeaders().asList();
        Map<String, String> someMap = new HashMap<>();

        for (Header header : headerList) {
            String headerName = header.getName();
            someMap.put((headerName), "");
        }

        String[] keys = someMap.keySet().toArray(new String[0]);
        String[] headersFromDoc = {"Keep-Alive", "Server", "Cache-Control", "Connection", "Expires", "Content-Length", "Date"};
        // test if true below
        // String[] headersFromDoc = {"Keep-Alive", "Server", "Cache-Control", "Connection", "Expires", "x-secret-homework-header", "Content-Length", "Date", "Content-Type"};

        HashSet<String> headersNotInDoc = new HashSet<String>(Arrays.asList(keys));
        headersNotInDoc.removeAll(Arrays.asList(headersFromDoc));
        Object [] myArr = headersNotInDoc.toArray();

        for (int i = 0; i < myArr.length; i++) {
            Headers headers = this.response.getHeaders();
            String badHeader = myArr[i].toString();
            String badHeaderValue = headers.getValue(myArr[i].toString().replaceAll("[\\[\\]]", ""));
            System.out.println("We have headers not in documentation: " + badHeader + " with value: " + badHeaderValue);
        }

        assertEquals(keys.length, headersFromDoc.length, "Response has additional headers: " + headersNotInDoc);

    }
}
