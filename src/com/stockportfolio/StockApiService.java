package com.stockportfolio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StockApiService {
    private static final String API_KEY = "1X1WGUD45L0N301Q";

    public static double getRealTimePrice(String symbol) {
        try {
            String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + API_KEY;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                json.append(line);
            }
            in.close();

            // ✅ Proper parsing
            JsonObject jsonObject = JsonParser.parseString(json.toString()).getAsJsonObject();
            JsonObject quote = jsonObject.getAsJsonObject("Global Quote");

            if (quote == null || !quote.has("05. price")) {
                throw new Exception("Invalid API response");
            }

            return Double.parseDouble(quote.get("05. price").getAsString());

        } catch (Exception e) {
            System.err.println("Error fetching price: " + e.getMessage());
            return -1;
        }
    }

}
