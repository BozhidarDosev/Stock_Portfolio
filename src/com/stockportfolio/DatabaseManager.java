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
        String selectSql = "SELECT quantity FROM stocks WHERE symbol = ?";
        try (Connection conn = connect();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setString(1, symbol);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                // Stock exists: update quantity
                int existingQty = rs.getInt("quantity");
                String updateSql = "UPDATE stocks SET quantity = ? WHERE symbol = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, existingQty + quantity);
                    updateStmt.setString(2, symbol);
                    updateStmt.executeUpdate();
                }
            } else {
                // New stock: insert a row
                String insertSql = "INSERT INTO stocks (symbol, quantity, price) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, symbol);
                    insertStmt.setInt(2, quantity);
                    insertStmt.setDouble(3, price);
                    insertStmt.executeUpdate();
                }
            }
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

    public void deleteStock(String symbol) {
        String sql = "DELETE FROM stocks WHERE symbol = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, symbol);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                // Optionally inform if no row was deleted
                System.out.println("No stock with symbol " + symbol + " found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
