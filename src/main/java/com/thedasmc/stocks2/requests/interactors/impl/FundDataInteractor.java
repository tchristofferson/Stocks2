package com.thedasmc.stocks2.requests.interactors.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thedasmc.stocks2.common.Constants;
import com.thedasmc.stocks2.common.Tools;
import com.thedasmc.stocks2.requests.interactors.AbstractFundDataInteractor;
import com.thedasmc.stocks2.requests.request.*;
import com.thedasmc.stocks2.requests.response.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static com.thedasmc.stocks2.common.Tools.*;

public class FundDataInteractor extends AbstractFundDataInteractor {

    private static final String FUND_ID_PLACEHOLDER = "%fundId%";
    private static final String SYMBOL_PLACEHOLDER = "%symbol%";
    private static final String FUND_STATUS_PLACEHOLDER = "%status%";
    private static final String PLAYER_ID_PLACEHOLDER = "%playerId%";

    private static final String CREATE_FUND_URI = "/v1/fund/create";
    private static final String ADD_STOCK_TO_FUND_URI = "/v1/fund/stocks/add";
    private static final String REMOVE_STOCK_FROM_FUND_URI = "/v1/fund/" + FUND_ID_PLACEHOLDER + "/" + SYMBOL_PLACEHOLDER + "/remove";
    private static final String UPDATE_FUND_STATUS_URI = "/v1/fund/" + FUND_ID_PLACEHOLDER + "/" + FUND_STATUS_PLACEHOLDER;
    private static final String TRANSACT_FUND_URI = "/v1/fund/transact";
    private static final String POPULAR_FUNDS_URI = "/v1/fund/popular";
    private static final String GET_FUND_URI = "/v1/fund/" + FUND_ID_PLACEHOLDER;
    private static final String GET_PLAYER_FUND_INVESTMENT_VALUE_URI = "/v1/fund/" + FUND_ID_PLACEHOLDER + "/" + PLAYER_ID_PLACEHOLDER + "/value";
    private static final String GET_FUNDS_BY_CREATOR_URI = "/v1/" + PLAYER_ID_PLACEHOLDER + "/funds";
    private static final String GET_FUND_PORTFOLIO_URI = "/v1/fund/portfolio";

    public FundDataInteractor(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    @Override
    public FundResponse createFund(CreateFundRequest request) throws IOException {
        URL url = new URL(Constants.API_URL + CREATE_FUND_URI);
        HttpURLConnection connection = Tools.getHttpPostConnection(url, this.apiToken);

        String requestJson = this.gson.toJson(request);
        writeBody(connection, requestJson);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));
        }

        return this.gson.fromJson(responseJson, FundResponse.class);
    }

    @Override
    public FundStockResponse addStockToFund(FundStockRequest request) throws IOException {
        URL url = new URL(Constants.API_URL + ADD_STOCK_TO_FUND_URI);
        HttpURLConnection connection = Tools.getHttpPostConnection(url, this.apiToken);

        String requestJson = this.gson.toJson(request);
        writeBody(connection, requestJson);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));
        }

        return this.gson.fromJson(responseJson, FundStockResponse.class);
    }

    @Override
    public void removeStockFromFund(long fundId, String symbol) throws IOException {
        URL url = new URL(Constants.API_URL +
            REMOVE_STOCK_FROM_FUND_URI
                .replace(FUND_ID_PLACEHOLDER, String.valueOf(fundId))
                .replace(SYMBOL_PLACEHOLDER, symbol)
        );

        HttpURLConnection connection = getHttpDeleteConnection(url, this.apiToken);

        try {
            connection.getResponseCode();
        } catch (IOException e) {
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));
        }
    }

    @Override
    public FundResponse updateFundStatus(long fundId, char status) throws IOException {
        URL url = new URL(Constants.API_URL +
            UPDATE_FUND_STATUS_URI
                .replace(FUND_ID_PLACEHOLDER, String.valueOf(fundId))
                .replace(FUND_STATUS_PLACEHOLDER, String.valueOf(status))
        );

        HttpURLConnection connection = getHttpPutConnection(url, this.apiToken);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));
        }

        return this.gson.fromJson(responseJson, FundResponse.class);
    }

    @Override
    public FundRecordResponse transactFund(FundTransactionRequest request) throws IOException {
        URL url = new URL(Constants.API_URL + TRANSACT_FUND_URI);
        HttpURLConnection connection = getHttpPostConnection(url, this.apiToken);

        String requestJson = this.gson.toJson(request);
        writeBody(connection, requestJson);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));
        }

        return this.gson.fromJson(responseJson, FundRecordResponse.class);
    }

    @Override
    public List<FundResponse> getPopularFunds(PageRequest request) throws IOException {
        URL url = new URL(Constants.API_URL + POPULAR_FUNDS_URI);
        HttpURLConnection connection = getHttpPostConnection(url, this.apiToken);

        String requestJson = this.gson.toJson(request);
        writeBody(connection, requestJson);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));
        }

        Type type = new TypeToken<List<FundResponse>>(){}.getType();
        return this.gson.fromJson(responseJson, type);
    }

    @Override
    public FundResponse getFund(long fundId) throws IOException {
        URL url = new URL(Constants.API_URL + GET_FUND_URI.replace(FUND_ID_PLACEHOLDER, String.valueOf(fundId)));
        HttpURLConnection connection = getHttpGetConnection(url, this.apiToken);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(Tools.readErrorStream(connection.getErrorStream()));
        }

        return this.gson.fromJson(responseJson, FundResponse.class);
    }

    @Override
    public FundValueResponse getPlayerFundInvestmentValue(long fundId, UUID playerId) throws IOException {
        URL url = new URL(Constants.API_URL + GET_PLAYER_FUND_INVESTMENT_VALUE_URI
            .replace(FUND_ID_PLACEHOLDER, String.valueOf(fundId))
            .replace(PLAYER_ID_PLACEHOLDER, playerId.toString())
        );

        HttpURLConnection connection = getHttpGetConnection(url, this.apiToken);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(readErrorStream(connection.getErrorStream()));
        }

        return this.gson.fromJson(responseJson, FundValueResponse.class);
    }

    @Override
    public List<FundResponse> getFundsByCreator(UUID creatorId) throws IOException {
        URL url = new URL(Constants.API_URL + GET_FUNDS_BY_CREATOR_URI.replace(PLAYER_ID_PLACEHOLDER, creatorId.toString()));
        HttpURLConnection connection = getHttpGetConnection(url, this.apiToken);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(readErrorStream(connection.getErrorStream()));
        }

        Type type = new TypeToken<List<FundResponse>>(){}.getType();
        return this.gson.fromJson(responseJson, type);
    }

    @Override
    public FundPortfolioResponse getFundPortfolio(UUID playerId, int page) throws IOException {
        URL url = new URL(Constants.API_URL + GET_FUND_PORTFOLIO_URI);
        HttpURLConnection connection = getHttpPostConnection(url, this.apiToken);
        FundPortfolioRequest request = new FundPortfolioRequest(playerId, page);
        String requestJson = this.gson.toJson(request);
        writeBody(connection, requestJson);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(readErrorStream(connection.getErrorStream()));
        }

        return this.gson.fromJson(responseJson, FundPortfolioResponse.class);
    }
}
