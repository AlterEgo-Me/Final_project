package org.example.controller;

import org.example.database.Database;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BalanceControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private Database database;

    @Test
    void getBalanceApiTest()
            throws IOException, InterruptedException, SQLException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:" + port + "/balance?user_id=1"
                ))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        BigDecimal expectedBalance = database.getBalance(1);
        BigDecimal actualBalance = new BigDecimal(response.body());

        assertEquals(expectedBalance, actualBalance);
    }
    @Test
    void putMoneyApiTest()
            throws IOException, InterruptedException, SQLException {

        BigDecimal balanceBefore = database.getBalance(1);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:" + port +
                                "/put?user_id=1&bigDecimal=100"
                ))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        BigDecimal balanceAfter = database.getBalance(1);
        BigDecimal expectedBalance =
                balanceBefore.add(new BigDecimal("100"));
        assertEquals(expectedBalance, balanceAfter);
        assertEquals(200, response.statusCode());
    }
    @Test
    void takeMoneyApiTest()
            throws IOException, InterruptedException, SQLException {

        BigDecimal balanceBefore = database.getBalance(1);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:" + port +
                                "/take?user_id=1&bigDecimal=100"
                ))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        BigDecimal balanceAfter = database.getBalance(1);
        BigDecimal expectedBalance =
                balanceBefore.subtract(new BigDecimal("100"));
        assertEquals(expectedBalance, balanceAfter);
        assertEquals(200, response.statusCode());
    }
    @Test
    void getOperationListApiTest()
            throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:" + port +
                                "/operations?user_id=1"
                ))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        assertEquals(200, response.statusCode());

        String body = response.body();

        assertFalse(body.isEmpty());

        assertTrue(body.contains("\"userId\":1"));
    }
    @Test
    void transferMoneyApiTest()
            throws IOException, InterruptedException, SQLException {

        BigDecimal balanceBeforeUser1 = database.getBalance(1);
        BigDecimal balanceBeforeUser2 = database.getBalance(2);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:" + port +
                                "/transfer?fromUserId=1&toUserId=2&bigDecimal=100"
                ))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        assertEquals(200, response.statusCode());


        BigDecimal balanceAfterUser1 = database.getBalance(1);
        BigDecimal balanceAfterUser2 = database.getBalance(2);


        BigDecimal expectedBalanceUser1 =
                balanceBeforeUser1.subtract(new BigDecimal("100"));

        BigDecimal expectedBalanceUser2 =
                balanceBeforeUser2.add(new BigDecimal("100"));


        assertEquals(expectedBalanceUser1, balanceAfterUser1);

        assertEquals(expectedBalanceUser2, balanceAfterUser2);
    }
}
