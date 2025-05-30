package com.stockportfolio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/stock_portfolio?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Loretina13!";

    public DatabaseManager() {
        createTableIfNotExists();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS stocks (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "symbol VARCHAR(10), " +
                "quantity INT, " +
                "price DECIMAL(10,2), " +
                "date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addStock(String symbol, int quantity, double price) {
        String sql = "INSERT INTO stocks (symbol, quantity, price) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, symbol);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, price);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllStocks() {
        List<String> stocks = new ArrayList<>();
        String sql = "SELECT symbol, quantity, price, date_added FROM stocks";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String s = String.format("%s - Qty: %d - $%.2f - Added: %s",
                        rs.getString("symbol"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getTimestamp("date_added").toString());
                stocks.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }
}
