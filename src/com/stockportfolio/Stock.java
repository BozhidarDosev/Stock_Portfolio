package com.stockportfolio;

public class Stock {
    private String symbol;
    private int quantity;

    public Stock(String symbol, int quantity) {
        this.symbol = symbol.toUpperCase();
        this.quantity = quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
