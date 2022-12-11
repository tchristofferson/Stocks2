package com.thedasmc.stocks2.requests.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.thedasmc.stocks2.common.Constants;
import com.thedasmc.stocks2.common.Tools;
import com.thedasmc.stocks2.requests.AbstractStockDataRequestor;
import com.thedasmc.stocks2.requests.response.StockDataResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.thedasmc.stocks2.common.Tools.readInputStream;

public class StockDataRequestor extends AbstractStockDataRequestor {

    private static final String SYMBOLS_PLACEHOLDER = "%symbols%";
    private static final String QUOTE_URI = "/v1/quotes?symbols=" + SYMBOLS_PLACEHOLDER + "&token=" + Constants.TOKEN_PLACEHOLDER;

    public StockDataRequestor(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public Map<String, StockDataResponse> getQuotes(Collection<String> symbols) throws IOException {
        URL url = new URL(getQuoteUrl(symbols));
        HttpURLConnection connection = Tools.getHttpGetConnection(url);
        String json = readInputStream(connection.getInputStream());
        Set<StockDataResponse> stockDataResponseSet = this.gson.fromJson(json, new TypeToken<HashSet<StockDataResponse>>(){}.getType());
        Map<String, StockDataResponse> stockDataMap = stockDataResponseSet.stream()
            .collect(Collectors.toMap(StockDataResponse::getSymbol, Function.identity()));

        symbols.forEach(symbol -> {
            if (!stockDataMap.containsKey(symbol))
                stockDataMap.put(symbol, null);
        });

        return stockDataMap;
    }

    private String getQuoteUrl(Collection<String> symbols) {
        return Constants.API_URL + QUOTE_URI
                .replace(SYMBOLS_PLACEHOLDER, String.join(",", symbols))
                .replace(Constants.TOKEN_PLACEHOLDER, this.apiToken);
    }
}
