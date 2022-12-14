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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import static com.thedasmc.stocks2.common.Tools.readInputStream;
import static com.thedasmc.stocks2.common.Tools.writeBody;

public class PlayerDataInteractor extends AbstractPlayerDataInteractor {

    private static final String PLAYER_ID_PLACEHOLDER = "%playerId%";
    private static final String SYMBOL_PLACEHOLDER = "%symbol%";
    private static final String RECORD_ID_PLACEHOLDER = "%recordId%";

    private static final String PORTFOLIO_URI = "/v1/player/portfolio";
    private static final String GET_STOCK_URI = "/v1/player/" + PLAYER_ID_PLACEHOLDER + "/" + SYMBOL_PLACEHOLDER + "?token=" + Constants.TOKEN_PLACEHOLDER;
    private static final String TRANSACT_URI = "/v1/player/transact";
    private static final String CANCEL_TRANSACTION_URI = "/v1/records/" + RECORD_ID_PLACEHOLDER + "/cancel";

    public PlayerDataInteractor(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    @Override
    public PortfolioResponse getPortfolio(UUID uuid, int page) throws IOException {
        URL url = new URL(Constants.API_URL + PORTFOLIO_URI);
        HttpURLConnection connection = Tools.getHttpPostConnection(url);

        PortfolioRequest request = new PortfolioRequest(this.apiToken, page, uuid);
        String requestJson = this.gson.toJson(request);
        writeBody(connection, requestJson);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));
        }

        return this.gson.fromJson(responseJson, PortfolioResponse.class);
    }

    @Override
    public StockResponse getStock(UUID uuid, String symbol) throws IOException {
        URL url = new URL(getStockUrl(uuid, symbol));
        HttpURLConnection connection = Tools.getHttpGetConnection(url);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));
        }

        return this.gson.fromJson(responseJson, StockResponse.class);
    }

    @Override
    public RecordResponse transact(RecordRequest recordRequest) throws IOException {
        recordRequest.setApiToken(this.apiToken);
        URL url = new URL(Constants.API_URL + TRANSACT_URI);
        HttpURLConnection connection = Tools.getHttpPostConnection(url);
        writeBody(connection, this.gson.toJson(recordRequest));
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));
        }

        return this.gson.fromJson(responseJson, RecordResponse.class);
    }

    @Override
    public Boolean cancelTransaction(Long recordId) throws IOException {
        URL url = new URL(getCancelTransactionUrl(recordId));
        HttpURLConnection connection = Tools.getHttpDeleteConnection(url);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));
        }

        return this.gson.fromJson(responseJson, Boolean.class);
    }

    private String getCancelTransactionUrl(Long recordId) {
        return Constants.API_URL + CANCEL_TRANSACTION_URI
            .replace(RECORD_ID_PLACEHOLDER, String.valueOf(recordId));
    }

    private String getStockUrl(UUID uuid, String symbol) {
        return Constants.API_URL + GET_STOCK_URI
            .replace(PLAYER_ID_PLACEHOLDER, uuid.toString())
            .replace(SYMBOL_PLACEHOLDER, symbol)
            .replace(Constants.TOKEN_PLACEHOLDER, this.apiToken);
    }
}
