package org.example.database;

import org.example.model.Operation;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class Database {
    Properties properties = new Properties();
    String url;
    String userName;
    String password;

    public Database() throws IOException {
        try (InputStream inputStream = Database.class.getClassLoader().getResourceAsStream("database.properties");) {
            if (inputStream == null) {
                throw new IllegalStateException("Файл не найден");
            }
            properties.load(inputStream);
            url = properties.getProperty("db.url");
            userName = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }

    public BigDecimal getBalance(int user_id) throws SQLException {

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT balance FROM accounts WHERE user_id = ?")) {

            statement.setInt(1, user_id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBigDecimal("balance");
            }

            throw new IllegalStateException(
                    "Данные с user_id: " + user_id + " не обнаружены"
            );
        }
    }

    public void putMoney(int user_id, BigDecimal amount) throws SQLException {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Сумма пополнения должна быть больше нуля"
            );
        }

        Connection connection = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE user_id = ?");
                 PreparedStatement operationStatement = connection.prepareStatement(
                         "INSERT INTO public.operations " +
                                 "(user_id, type, amount, operation_date) " +
                                 "VALUES (?, ?, ?, CURRENT_TIMESTAMP)")
            ) {

                statement.setBigDecimal(1, amount);
                statement.setInt(2, user_id);

                int updatedRows = statement.executeUpdate();

                if (updatedRows == 0) {
                    throw new IllegalStateException(
                            "Данные с user_id: " + user_id + " не обнаружены"
                    );
                }

                operationStatement.setInt(1, user_id);
                operationStatement.setInt(2, 1); // 1 - пополнение
                operationStatement.setBigDecimal(3, amount);

                operationStatement.executeUpdate();

                connection.commit();
            }

        } catch (Exception e) {

            if (connection != null) {
                connection.rollback();
            }

            throw e;

        } finally {

            if (connection != null) {
                connection.close();
            }
        }
    }


    public void takeMoney(int user_id, BigDecimal amount) throws SQLException {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Сумма снятия должна быть больше нуля"
            );
        }

        Connection connection = null;

        try {

            connection = getConnection();
            connection.setAutoCommit(false);


            try (PreparedStatement balanceStatement = connection.prepareStatement(
                    "SELECT balance FROM accounts WHERE user_id = ?");

                 PreparedStatement updateStatement = connection.prepareStatement(
                         "UPDATE accounts SET balance = balance - ? WHERE user_id = ?");

                 PreparedStatement operationStatement = connection.prepareStatement(
                         "INSERT INTO public.operations " +
                                 "(user_id, type, amount, operation_date) " +
                                 "VALUES (?, ?, ?, CURRENT_TIMESTAMP)")
            ) {


                balanceStatement.setInt(1, user_id);

                ResultSet resultSet = balanceStatement.executeQuery();


                if (!resultSet.next()) {
                    throw new IllegalStateException(
                            "Пользователь не найден"
                    );
                }


                BigDecimal balance = resultSet.getBigDecimal("balance");


                if (balance.compareTo(amount) < 0) {
                    throw new IllegalStateException(
                            "Баланс слишком мал для вычета"
                    );
                }


                updateStatement.setBigDecimal(1, amount);
                updateStatement.setInt(2, user_id);

                updateStatement.executeUpdate();


                operationStatement.setInt(1, user_id);
                operationStatement.setInt(2, 2);
                operationStatement.setBigDecimal(3, amount);

                operationStatement.executeUpdate();


                connection.commit();

            }


        } catch (Exception e) {

            if(connection != null){
                connection.rollback();
            }

            throw e;

        } finally {

            if(connection != null){
                connection.close();
            }
        }
    }

    public List<Operation> getOperationList(int userId, LocalDateTime fromDate, LocalDateTime toDate) throws SQLException {
        List<Operation> operations = new ArrayList<>();
        boolean dateFlag;
        String sql;

        if (fromDate != null && toDate != null) {
            sql = "SELECT * FROM operations WHERE user_id = ? " +
                    "AND operation_date BETWEEN ? AND ?";
            dateFlag = true;
        } else {
            sql = "SELECT * FROM operations WHERE user_id = ?";
            dateFlag = false;
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            if (dateFlag) {
                statement.setTimestamp(2, Timestamp.valueOf(fromDate));
                statement.setTimestamp(3, Timestamp.valueOf(toDate));
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int operationId = resultSet.getInt("operation_id");
                int userIdFromDb = resultSet.getInt("user_id");
                int type = resultSet.getInt("type");
                BigDecimal amount = resultSet.getBigDecimal("amount");

                Timestamp timestamp = resultSet.getTimestamp("operation_date");
                LocalDateTime operationDate = timestamp.toLocalDateTime();

                Operation operation = new Operation(operationId, userIdFromDb, type, amount, operationDate);
                operations.add(operation);
            }
        }

        return operations;
    }

    public void transferMoney(int fromUserId, int toUserId, BigDecimal amount) throws SQLException {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть больше нуля");
        }

        Connection connection = getConnection();

        try (PreparedStatement balanceStatement = connection.prepareStatement(
                "SELECT balance FROM accounts WHERE user_id = ?");

             PreparedStatement fromStatement = connection.prepareStatement(
                     "UPDATE accounts SET balance = balance - ? WHERE user_id = ?");

             PreparedStatement toStatement = connection.prepareStatement(
                     "UPDATE accounts SET balance = balance + ? WHERE user_id = ?");

             PreparedStatement operationStatement = connection.prepareStatement(
                     "INSERT INTO public.operations " +
                             "(user_id, target_user_id, type, amount, operation_date) " +
                             "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)")) {

            connection.setAutoCommit(false);


            balanceStatement.setInt(1, fromUserId);

            ResultSet resultSet = balanceStatement.executeQuery();

            if (!resultSet.next()) {
                throw new IllegalStateException(
                        "Пользователь-отправитель не существует"
                );
            }

            BigDecimal balance = resultSet.getBigDecimal("balance");

            if (balance.compareTo(amount) < 0) {
                throw new IllegalStateException(
                        "Баланс слишком мал для перевода"
                );
            }


            fromStatement.setBigDecimal(1, amount);
            fromStatement.setInt(2, fromUserId);

            int updatedRows = fromStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new IllegalStateException(
                        "Пользователь-отправитель не существует"
                );
            }


            toStatement.setBigDecimal(1, amount);
            toStatement.setInt(2, toUserId);

            updatedRows = toStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new IllegalStateException(
                        "Пользователь-получатель не существует"
                );
            }

            operationStatement.setInt(1, fromUserId);
            operationStatement.setInt(2, toUserId);
            operationStatement.setInt(3, 3);
            operationStatement.setBigDecimal(4, amount);

            operationStatement.executeUpdate();

            connection.commit();

        } catch (Exception e) {

            connection.rollback();
            throw e;

        } finally {

            connection.close();
        }
    }
}
