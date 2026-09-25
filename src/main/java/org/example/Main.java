package org.example;

import org.example.database.Database;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException, SQLException {

        ApplicationContext context = SpringApplication.run(Main.class, args);
        Database database = context.getBean(Database.class);

    }
}