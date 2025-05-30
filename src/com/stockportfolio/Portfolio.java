package com.stockportfolio;

import com.stockportfolio.Stock;
import com.stockportfolio.StockApiService;

import java.util.*;

public class Portfolio {
    private List<Stock> stocks = new ArrayList<>();

    public void addStock(Stock stock) {
        for (Stock s : stocks) {
            if (s.getSymbol().equals(stock.getSymbol())) {
                s.setQuantity(s.getQuantity() + stock.getQuantity());
                return;
            }
        }
        stocks.add(stock);
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public double getTotalValue() {
        double total = 0;
        for (Stock stock : stocks) {
            double price = StockApiService.getRealTimePrice(stock.getSymbol());
            if (price > 0) {
                total += stock.getQuantity() * price;
            }
        }
        return total;
    }
}
