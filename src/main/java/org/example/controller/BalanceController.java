package org.example.controller;
import org.example.model.Operation;
import java.time.LocalDateTime;
import java.util.List;
import org.example.database.Database;
import org.example.model.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.SQLException;

@RestController
public class BalanceController {
    Database database;


    @GetMapping("/balance")
    public BigDecimal getBalanceApi(@RequestParam int user_id) throws SQLException {
        return database.getBalance(user_id);
    }

    @PostMapping("/put")
    public void putMoneyApi(@RequestParam int user_id, @RequestParam BigDecimal bigDecimal) throws SQLException {
        database.putMoney(user_id, bigDecimal);
    }
    @PostMapping("/take")
    public void takeMoneyApi(@RequestParam int user_id, @RequestParam BigDecimal bigDecimal ) throws SQLException {
        database.takeMoney(user_id, bigDecimal);
    }

    @GetMapping("/operations")
    public List<Operation> getOperationListApi(
            @RequestParam int user_id,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate
    ) throws SQLException {

        return database.getOperationList(user_id, fromDate, toDate);
    }

    public BalanceController(Database database) {
        this.database = database;
    }
}
