/*
Ex12: Тест запроса на метод header

Необходимо написать тест, который делает запрос на метод: https://playground.learnqa.ru/api/homework_header

Этот метод возвращает headers с каким-то значением.
Необходимо понять что за headers и с каким значением, и зафиксировать это поведение с помощью assert

Результатом должна быть ссылка на коммит с тестом.
 */
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

        // Предположим у нас в ТЗ есть список заголовков, сверим этот список с реальным
        String[] keys = someMap.keySet().toArray(new String[0]);
        String[] headersFromDoc = {
                "Keep-Alive", "Server", "Cache-Control", "Connection", "Expires", "x-secret-homework-header", "Content-Type", "Content-Length", "Date"
        };

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

    @Test
    public void checkSecretHeader() {
        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders);
        String responseHeader = response.getHeader("x-secret-homework-header");

        assertEquals("Some secret value", responseHeader, "Unexpected header value");
    }
}
