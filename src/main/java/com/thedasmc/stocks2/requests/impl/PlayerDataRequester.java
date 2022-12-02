package com.thedasmc.stocks2.requests.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.thedasmc.stocks2.common.Constants;
import com.thedasmc.stocks2.common.Tools;
import com.thedasmc.stocks2.requests.AbstractPlayerDataRequester;
import com.thedasmc.stocks2.requests.request.PortfolioRequest;
import com.thedasmc.stocks2.requests.response.StockResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDataRequester extends AbstractPlayerDataRequester {

    private static final String PORTFOLIO_URI = "/v1/player/portfolio";

    public PlayerDataRequester(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public List<StockResponse> getPortfolio(UUID uuid, int page) throws IOException {
        String urlString = Constants.API_URL + PORTFOLIO_URI;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");

        PortfolioRequest request = new PortfolioRequest(this.apiToken, page, uuid);
        String requestJson = this.gson.toJson(request);
        byte[] input = requestJson.getBytes(StandardCharsets.UTF_8);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(input);
        outputStream.flush();
        outputStream.close();
        String responseJson = Tools.readInputStream(connection.getInputStream());
        connection.disconnect();
        return this.gson.fromJson(responseJson, new TypeToken<ArrayList<StockResponse>>(){}.getType());
    }

    @Override
    public StockResponse getStock(UUID uuid, String symbol) {
        return null;
    }
}
