package com.thedasmc.stocks2.requests.impl;

import com.google.gson.Gson;
import com.thedasmc.stocks2.common.Constants;
import com.thedasmc.stocks2.common.Tools;
import com.thedasmc.stocks2.requests.AbstractPlayerDataInteractor;
import com.thedasmc.stocks2.requests.request.PortfolioRequest;
import com.thedasmc.stocks2.requests.request.RecordRequest;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import com.thedasmc.stocks2.requests.response.RecordResponse;
import com.thedasmc.stocks2.requests.response.StockResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.thedasmc.stocks2.common.Tools.readInputStream;

public class PlayerDataInteractor extends AbstractPlayerDataInteractor {

    private static final String PLAYER_ID_PLACEHOLDER = "%playerId%";
    private static final String SYMBOL_PLACEHOLDER = "%symbol%";

    private static final String PORTFOLIO_URI = "/v1/player/portfolio";
    private static final String GET_STOCK_URI = "/v1/player/" + PLAYER_ID_PLACEHOLDER + "/" + SYMBOL_PLACEHOLDER + "?token=" + Constants.TOKEN_PLACEHOLDER;
    private static final String TRANSACT_URI = "/v1/player/transact";

    public PlayerDataInteractor(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    @Override
    public PortfolioResponse getPortfolio(UUID uuid, int page) throws IOException {
        URL url = new URL(Constants.API_URL + PORTFOLIO_URI);
        HttpURLConnection connection = Tools.getHttpPostConnection(url);

        PortfolioRequest request = new PortfolioRequest(this.apiToken, page, uuid);
        String requestJson = this.gson.toJson(request);
        byte[] requestBytes = requestJson.getBytes(StandardCharsets.UTF_8);

        connection.setFixedLengthStreamingMode(requestBytes.length);
        try(OutputStream os = connection.getOutputStream()) {
            os.write(requestBytes);
        }

        String responseJson = readInputStream(connection.getInputStream());

        return this.gson.fromJson(responseJson, PortfolioResponse.class);
    }

    @Override
    public StockResponse getStock(UUID uuid, String symbol) throws IOException {
        URL url = new URL(getStockUrl(uuid, symbol));
        HttpURLConnection connection = Tools.getHttpGetConnection(url);
        String responseJson = readInputStream(connection.getInputStream());

        return this.gson.fromJson(responseJson, StockResponse.class);
    }

    @Override
    public RecordResponse transact(RecordRequest recordRequest) throws IOException {
        URL url = new URL(Constants.API_URL + TRANSACT_URI);
        HttpURLConnection connection = Tools.getHttpPostConnection(url);
        String responseJson = readInputStream(connection.getInputStream());

        return this.gson.fromJson(responseJson, RecordResponse.class);
    }

    private String getStockUrl(UUID uuid, String symbol) {
        return Constants.API_URL + GET_STOCK_URI
            .replace(PLAYER_ID_PLACEHOLDER, uuid.toString())
            .replace(SYMBOL_PLACEHOLDER, symbol)
            .replace(Constants.TOKEN_PLACEHOLDER, this.apiToken);
    }
}
