package com.stockportfolio;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PortfolioUI {
    private Portfolio portfolio = new Portfolio();
    private DatabaseManager dbManager = new DatabaseManager();

    public void showUI() {
        JFrame frame = new JFrame("Stock Portfolio Tracker");
        frame.setSize(450, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField symbolField = new JTextField(5);
        JTextField quantityField = new JTextField(5);
        JButton addButton = new JButton("Add Stock");
        JButton showButton = new JButton("Show My Portfolio");
        JTextArea displayArea = new JTextArea(15, 35);
        JButton calculateButton = new JButton("Calculate Total Value");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Symbol:"));
        panel.add(symbolField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(addButton);
        panel.add(showButton);
        panel.add(calculateButton);
        panel.add(new JScrollPane(displayArea));

        addButton.addActionListener(e -> {
            try {
                String symbol = symbolField.getText().trim().toUpperCase();
                int qty = Integer.parseInt(quantityField.getText().trim());
                if (symbol.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a stock symbol.");
                    return;
                }
                // Add to in-memory portfolio
                portfolio.addStock(new Stock(symbol, qty));
                // Save to MySQL
                dbManager.addStock(symbol, qty, 0); // Here 0 as price, you can adapt your Stock class for price if needed
                displayArea.append("Added: " + symbol + " x" + qty + "\n");

                symbolField.setText("");
                quantityField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid quantity.");
            }
        });

        showButton.addActionListener(e -> {
            displayArea.setText("");
            List<String> stocksFromDB = dbManager.getAllStocks();
            if (stocksFromDB.isEmpty()) {
                displayArea.append("Portfolio is empty.\n");
            } else {
                for (String stockStr : stocksFromDB) {
                    displayArea.append(stockStr + "\n");
                }
            }
        });

        calculateButton.addActionListener(e -> {
            double total = portfolio.getTotalValue();
            displayArea.append("Total Portfolio Value: $" + total + "\n");
        });

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}
