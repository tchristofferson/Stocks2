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
        HttpURLConnection connection = Tools.getHttpPostConnection(url);

        PortfolioRequest request = new PortfolioRequest(this.apiToken, page, uuid);
        String requestJson = this.gson.toJson(request);
        byte[] requestBytes = requestJson.getBytes(StandardCharsets.UTF_8);

        connection.setFixedLengthStreamingMode(requestBytes.length);
        try(OutputStream os = connection.getOutputStream()) {
            os.write(requestBytes);
        }

        String responseJson = Tools.readInputStream(connection.getInputStream());

        return this.gson.fromJson(responseJson, new TypeToken<ArrayList<StockResponse>>(){}.getType());
    }

    @Override
    public StockResponse getStock(UUID uuid, String symbol) {
        return null;
    }
}
