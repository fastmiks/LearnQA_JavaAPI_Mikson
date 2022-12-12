/* Ex9: Подбор пароля

        Сегодня к нам пришел наш коллега и сказал, что забыл свой пароль от важного сервиса. Он просит нас помочь ему написать программу, которая подберет его пароль.

        Условие следующее.
        Есть метод: https://playground.learnqa.ru/ajax/api/get_secret_password_homework
        Его необходимо вызывать POST-запросом с двумя параметрами: login и password
        Если вызвать метод без поля login или указать несуществующий login, метод вернет 500
        Если login указан и существует, метод вернет нам авторизационную cookie с названием auth_cookie и каким-то значением.

        У метода существует защита от перебора. Если верно указано поле login, но передан неправильный password, то авторизационная cookie все равно вернется.
        НО с "неправильным" значением, которое на самом деле не позволит создавать авторизованные запросы.
        Только если и login, и password указаны верно, вернется cookie с "правильным" значением.
        Таким образом используя только метод get_secret_password_homework невозможно узнать, передали ли мы верный пароль или нет.

        По этой причине нам потребуется второй метод, который проверяет правильность нашей авторизованной cookie: https://playground.learnqa.ru/ajax/api/check_auth_cookie
        Если вызвать его без cookie с именем auth_cookie или с cookie, у которой выставлено "неправильное" значение, метод вернет фразу "You are NOT authorized".
        Если значение cookie “правильное”, метод вернет: “You are authorized”

        Коллега говорит, что точно помнит свой login - это значение super_admin
        А вот пароль забыл, но точно помнит, что выбрал его из списка самых популярных паролей на Википедии (вот тебе и супер админ...).

        Ссылка: https://en.wikipedia.org/wiki/List_of_the_most_common_passwords

        Искать его нужно среди списка Top 25 most common passwords by year according to SplashData
        - список паролей можно скопировать в ваш тест вручную или придумать более хитрый способ, если сможете.

        Итак, наша задача - написать тест и указать в нем login нашего коллеги и все пароли из Википедии в виде списка. Программа должна делать следующее:
        1. Брать очередной пароль и вместе с логином коллеги вызывать первый метод get_secret_password_homework.
        В ответ метод будет возвращать авторизационную cookie с именем auth_cookie и каким-то значением.

        2. Далее эту cookie мы должна передать во второй метод check_auth_cookie.
        Если в ответ вернулась фраза "You are NOT authorized", значит пароль неправильный. В этом случае берем следующий пароль и все заново.
        Если же вернулась другая фраза - нужно, чтобы программа вывела верный пароль и эту фразу.

        Ответом к задаче должен быть верный пароль и ссылка на коммит со скриптом.
 */
package HOMEWORK;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Ex9_Look_at_you_hacker {

    @Test
    public void findPass() {
        // я слишком глуп, поэтому список паролей вручную
        ArrayList<String> passwords = new ArrayList<>();
        passwords.add("123456");
        passwords.add("123456789");
        passwords.add("qwerty");
        passwords.add("password");
        passwords.add("1234567");
        passwords.add("12345678");
        passwords.add("12345");
        passwords.add("iloveyou");
        passwords.add("111111");
        passwords.add("123123");
        passwords.add("abc123");
        passwords.add("qwerty123");
        passwords.add("1q2w3e4r");
        passwords.add("admin");
        passwords.add("qwertyuiop");
        passwords.add("654321");
        passwords.add("555555");
        passwords.add("lovely");
        passwords.add("7777777");
        passwords.add("welcome");
        passwords.add("888888");
        passwords.add("princess");
        passwords.add("dragon");
        passwords.add("password1");
        passwords.add("123qwe");

        for (String password : passwords) { // цикл со стрингами
            Map<String, String> auth = new HashMap<>();
            auth.put("login", "super_admin");
            auth.put("password", password);

            Response response = RestAssured
                    .given()
                    .body(auth)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String cookie = response.getCookie("auth_cookie");

            Response checkAuth = RestAssured
                    .given()
                    .cookie("auth_cookie", cookie)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();


            System.out.println("My auth cookie is: " + cookie);
            String answer = checkAuth.print();

            if (answer.equals("You are NOT authorized")) { // оказывается строки это ссылочный тип данных ?"*№ь
                System.out.println("Password used:" + password + "\nTry again\n");
            } else {
                System.out.println("Password used:" + password);
                break;
            }
        }
    }
}
