package org.example.database;

import org.example.model.Operation;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    @Test
    void getBalanceTest() throws IOException, SQLException {

        Database database = new Database();

        BigDecimal balance = database.getBalance(1);

        assertNotNull(balance);
    }

    @Test
    void putMoneyTest() throws IOException, SQLException {

        Database database = new Database();

        BigDecimal balanceBefore = database.getBalance(1);

        database.putMoney(1, new BigDecimal("100"));

        BigDecimal balanceAfter = database.getBalance(1);

        BigDecimal expectedBalance =
                balanceBefore.add(new BigDecimal("100"));

        assertEquals(expectedBalance, balanceAfter);
    }
    @Test
    void takeMoneyTest() throws IOException, SQLException {

        Database database = new Database();

        BigDecimal balanceBefore = database.getBalance(1);

        database.takeMoney(1, new BigDecimal("100"));

        BigDecimal balanceAfter = database.getBalance(1);

        BigDecimal expectedBalance =
                balanceBefore.subtract(new BigDecimal("100"));

        assertEquals(expectedBalance, balanceAfter);
    }
    @Test
    void transferMoneyTest() throws IOException, SQLException {

        Database database = new Database();

        // Сначала гарантированно добавляем деньги отправителю
        database.putMoney(1, new BigDecimal("100"));

        BigDecimal balanceBefore1 = database.getBalance(1);
        BigDecimal balanceBefore2 = database.getBalance(2);

        database.transferMoney(1, 2, new BigDecimal("100"));

        BigDecimal balanceAfter1 = database.getBalance(1);
        BigDecimal balanceAfter2 = database.getBalance(2);

        BigDecimal expectedBalance1 =
                balanceBefore1.subtract(new BigDecimal("100"));

        BigDecimal expectedBalance2 =
                balanceBefore2.add(new BigDecimal("100"));

        assertEquals(expectedBalance1, balanceAfter1);
        assertEquals(expectedBalance2, balanceAfter2);
    }
    @Test
    void getOperationListTest() throws IOException, SQLException {

        Database database = new Database();

        List<Operation> operations =
                database.getOperationList(1, null, null);

        assertFalse(operations.isEmpty());

        Operation operation = operations.get(0);

        assertEquals(1, operation.getUserId());
    }
    @Test
    void takeMoneyNotEnoughMoneyTest() throws IOException, SQLException {

        Database database = new Database();

        assertThrows(
                IllegalStateException.class, () -> database.takeMoney(1, new BigDecimal("1000000"))
        );
    }
    @Test
    void transferMoneyNotEnoughMoneyTest() throws IOException, SQLException {

        Database database = new Database();

        assertThrows(
                IllegalStateException.class, () -> database.transferMoney(1, 2, new BigDecimal("1000000"))
        );
    }
    @Test
    void putMoneyUserNotFoundTest() throws IOException, SQLException {

        Database database = new Database();

        assertThrows(
                IllegalStateException.class, () -> database.putMoney(999, new BigDecimal("100"))
        );
    }
}