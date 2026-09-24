package org.example;

import org.example.database.Database;
import org.example.model.Operation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException, SQLException {

        ApplicationContext context = SpringApplication.run(Main.class, args);
        Database database = context.getBean(Database.class);
        LocalDateTime fromDate = LocalDateTime.of(2026, 9, 22, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(2026, 9, 22, 23, 59);

        List<Operation> operations =
                database.getOperationList(1, fromDate, toDate);

        for (Operation operation : operations) {
            System.out.println(
                    operation.getOperationId() + " | " + operation.getUserId() + " | " + operation.getType() + " | " +
                            operation.getAmount() + " | " +
                            operation.getOperationDate()
            );
        }
    }
}