package com.stockportfolio;

import com.stockportfolio.Portfolio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PortfolioUI {
    private Portfolio portfolio = new Portfolio();

    public void showUI() {
        JFrame frame = new JFrame("Stock Portfolio Tracker");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField symbolField = new JTextField(5);
        JTextField quantityField = new JTextField(5);
        JButton addButton = new JButton("Add Stock");
        JTextArea displayArea = new JTextArea(10, 30);
        JButton calculateButton = new JButton("Calculate Total Value");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Symbol:"));
        panel.add(symbolField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(addButton);
        panel.add(calculateButton);
        panel.add(new JScrollPane(displayArea));

        addButton.addActionListener(e -> {
            String symbol = symbolField.getText();
            int qty = Integer.parseInt(quantityField.getText());
            portfolio.addStock(new Stock(symbol, qty));
            displayArea.append("Added: " + symbol + " x" + qty + "\n");
        });

        calculateButton.addActionListener(e -> {
            double total = portfolio.getTotalValue();
            displayArea.append("Total Portfolio Value: $" + total + "\n");
        });

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}
