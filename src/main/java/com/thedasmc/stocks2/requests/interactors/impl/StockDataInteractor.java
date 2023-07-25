package com.thedasmc.stocks2.requests.interactors.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.thedasmc.stocks2.common.Constants;
import com.thedasmc.stocks2.common.Tools;
import com.thedasmc.stocks2.requests.interactors.AbstractStockDataInteractor;
import com.thedasmc.stocks2.requests.request.PageRequest;
import com.thedasmc.stocks2.requests.response.StockDataResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.thedasmc.stocks2.common.Tools.*;

public class StockDataInteractor extends AbstractStockDataInteractor {

    private static final String SYMBOLS_PLACEHOLDER = "%symbols%";
    private static final String QUOTE_URI = "/v1/quotes?symbols=" + SYMBOLS_PLACEHOLDER;
    private static final String POPULAR_URI = "/v1/stocks/popular";

    public StockDataInteractor(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public Map<String, StockDataResponse> getQuotes(Collection<String> symbols) throws IOException {
        URL url = new URL(getQuoteUrl(symbols));
        HttpURLConnection connection = getHttpGetConnection(url, this.apiToken);
        String json;

        try {
            json = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));
        }

        Set<StockDataResponse> stockDataResponseSet = this.gson.fromJson(json, new TypeToken<HashSet<StockDataResponse>>(){}.getType());
        Map<String, StockDataResponse> stockDataMap = stockDataResponseSet.stream()
            .collect(Collectors.toMap(StockDataResponse::getSymbol, Function.identity()));

        symbols.stream()
            .map(symbol -> symbol.trim().toUpperCase())
            .filter(symbol -> !stockDataMap.containsKey(symbol))
            .forEach(symbol -> stockDataMap.put(symbol, null));

        return stockDataMap;
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public List<StockDataResponse> getPopularStocks() throws IOException {
        URL url = new URL(Constants.API_URL + POPULAR_URI);
        HttpURLConnection connection = getHttpPostConnection(url, this.apiToken);

        PageRequest request = new PageRequest(0, Constants.STOCK_GUI_MAX);
        String requestJson = this.gson.toJson(request);
        writeBody(connection, requestJson);
        String json;

        try {
            json = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));
        }

        return this.gson.fromJson(json, new TypeToken<LinkedList<StockDataResponse>>(){}.getType());
    }

    private String getQuoteUrl(Collection<String> symbols) {
        return Constants.API_URL + QUOTE_URI
                .replace(SYMBOLS_PLACEHOLDER, String.join(",", symbols));
    }
}
