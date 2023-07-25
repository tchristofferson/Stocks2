package com.thedasmc.stocks2.requests.interactors.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.thedasmc.stocks2.common.Constants;
import com.thedasmc.stocks2.common.Tools;
import com.thedasmc.stocks2.requests.interactors.AbstractPlayerDataInteractor;
import com.thedasmc.stocks2.requests.request.DeleteRecordsRequest;
import com.thedasmc.stocks2.requests.request.PortfolioRequest;
import com.thedasmc.stocks2.requests.request.RecordRequest;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import com.thedasmc.stocks2.requests.response.RecordResponse;
import com.thedasmc.stocks2.requests.response.ShareSummaryResponse;
import com.thedasmc.stocks2.requests.response.StockResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static com.thedasmc.stocks2.common.Tools.readInputStream;
import static com.thedasmc.stocks2.common.Tools.writeBody;

public class PlayerDataInteractor extends AbstractPlayerDataInteractor {

    private static final String PLAYER_ID_PLACEHOLDER = "%playerId%";
    private static final String SYMBOL_PLACEHOLDER = "%symbol%";

    private static final String PORTFOLIO_URI = "/v1/player/portfolio";
    private static final String GET_STOCK_URI = "/v1/player/" + PLAYER_ID_PLACEHOLDER + "/" + SYMBOL_PLACEHOLDER;
    private static final String TRANSACT_URI = "/v1/player/transact";
    private static final String SHARE_SUMMARIES_URI = "/v1/player/" + PLAYER_ID_PLACEHOLDER + "/share-summaries";
    private static final String DELETE_RECORDS_URI = "/v1/player/records/delete";

    public PlayerDataInteractor(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    @Override
    public PortfolioResponse getPortfolio(UUID uuid, int page) throws IOException {
        URL url = new URL(Constants.API_URL + PORTFOLIO_URI);
        HttpURLConnection connection = Tools.getHttpPostConnection(url, this.apiToken);

        PortfolioRequest request = new PortfolioRequest(page, uuid);
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
        HttpURLConnection connection = Tools.getHttpGetConnection(url, this.apiToken);
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
        URL url = new URL(Constants.API_URL + TRANSACT_URI);
        HttpURLConnection connection = Tools.getHttpPostConnection(url, this.apiToken);
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
    @SuppressWarnings("UnstableApiUsage")
    public List<ShareSummaryResponse> getShareSummaries(UUID playerId) throws IOException {
        URL url = new URL(getShareSummariesUrl(playerId));
        HttpURLConnection connection = Tools.getHttpGetConnection(url, this.apiToken);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));
        }

        Type type = new TypeToken<List<ShareSummaryResponse>>(){}.getType();
        return this.gson.fromJson(responseJson, type);
    }

    @Override
    public boolean deleteRecords(DeleteRecordsRequest request) throws IOException {
        URL url = new URL(Constants.API_URL + DELETE_RECORDS_URI);
        HttpURLConnection connection = Tools.getHttpPostConnection(url, this.apiToken);
        writeBody(connection, this.gson.toJson(request));

        int responseCode;

        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }

        if (responseCode != HttpURLConnection.HTTP_OK)
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));

        return true;
    }

    private String getStockUrl(UUID uuid, String symbol) {
        return Constants.API_URL + GET_STOCK_URI
            .replace(PLAYER_ID_PLACEHOLDER, uuid.toString())
            .replace(SYMBOL_PLACEHOLDER, symbol);
    }

    private String getShareSummariesUrl(UUID uuid) {
        return Constants.API_URL + SHARE_SUMMARIES_URI
            .replace(PLAYER_ID_PLACEHOLDER, uuid.toString());
    }
}
