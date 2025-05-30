package com.stockportfolio;

import java.sql.*;

public class MySQLExample {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/stock_portfolio";
        String user = "root";
        String password = "Loretina13!";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO stocks (symbol, quantity, price) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "AAPL");
                stmt.setInt(2, 10);
                stmt.setDouble(3, 150.25);
                stmt.executeUpdate();
                System.out.println("Stock inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
