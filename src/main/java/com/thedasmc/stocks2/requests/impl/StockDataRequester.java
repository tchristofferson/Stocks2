package com.thedasmc.stocks2.requests.impl;

import com.google.common.reflect.TypeToken;
import com.thedasmc.stocks2.Constants;
import com.thedasmc.stocks2.requests.AbstractStockDataRequester;
import com.thedasmc.stocks2.requests.models.StockData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.thedasmc.stocks2.util.JsonTools.readJson;

public class StockDataRequester extends AbstractStockDataRequester {

    private static final String SYMBOLS_PLACEHOLDER = "%symbols%";
    private static final String TOKEN_PLACEHOLDER = "%token%";

    private static final String API_URL = "https://api.thedasmc.com";
    private static final String QUOTE_URI = "/v1/quotes?symbols=" + SYMBOLS_PLACEHOLDER + "&token=" + TOKEN_PLACEHOLDER;

    public StockDataRequester(List<String> symbols, String token) {
        super(symbols, token);
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public Map<String, StockData> getQuotes() throws IOException {
        String url = getQuoteUrl();
        HttpURLConnection connection = getConnection(url, "GET");
        String json = readJson(connection.getInputStream());
        Set<StockData> stockDataSet = Constants.getGson().fromJson(json, new TypeToken<HashSet<StockData>>(){}.getType());
        Map<String, StockData> stockDataMap = stockDataSet.stream()
            .collect(Collectors.toMap(StockData::getSymbol, Function.identity()));

        symbols.forEach(symbol -> {
            if (!stockDataMap.containsKey(symbol)) {
                stockDataMap.put(symbol, null);
            }
        });

        return stockDataMap;
    }

    private HttpURLConnection getConnection(String urlString, String method) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Accept", "application/json");

        return connection;
    }

    private String getQuoteUrl() {
        return API_URL + QUOTE_URI
                .replace(SYMBOLS_PLACEHOLDER, String.join(",", this.symbols))
                .replace(TOKEN_PLACEHOLDER, this.token);
    }
}
