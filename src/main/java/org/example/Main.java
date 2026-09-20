package org.example;

import org.example.database.Database;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.io.IOException;
import java.sql.SQLException;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException, SQLException {

        ApplicationContext context = SpringApplication.run(Main.class, args);

        Database database = context.getBean(Database.class);

        System.out.println(database.getBalance(1));

        database.putMoney(1, new BigDecimal("100"));
        System.out.println(database.getBalance(1));

        database.takeMoney(1, new BigDecimal("100"));
        System.out.println(database.getBalance(1));
    }
}