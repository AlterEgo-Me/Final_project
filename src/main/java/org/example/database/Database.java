package org.example.database;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
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
        return DriverManager.getConnection(url,userName,password);
    }

    public BigDecimal getBalance(int user_id) throws SQLException {
      try(Connection connection = getConnection(); Statement statement = connection.createStatement();
          ResultSet resultSet = statement.executeQuery("SELECT balance FROM accounts WHERE user_id = "+ user_id + ";")) {
          if(resultSet.next()){
             return resultSet.getBigDecimal("balance");
          }
          throw new IllegalStateException("Данные с user_id: " + user_id +" не обнаружены");
      }

    }
    public void putMoney(int user_id, BigDecimal amount) throws SQLException {

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE user_id = ?")) {

            statement.setBigDecimal(1, amount);
            statement.setInt(2, user_id);

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new IllegalStateException("Данные с user_id: " + user_id + " не обнаружены");
            }
        }
    }
    public void takeMoney(int user_id, BigDecimal amount) throws SQLException {
       int checkBalance = getBalance(user_id).compareTo(amount);
        if( checkBalance < 0){
            throw new IllegalStateException("Баланс слишком мал для вычета");
        }
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE user_id = ?")) {

            statement.setBigDecimal(1, amount);
            statement.setInt(2, user_id);

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new IllegalStateException("Данные с user_id: " + user_id + " не обнаружены");
            }

        }
    }
}
