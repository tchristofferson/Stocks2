package com.thedasmc.stocks2.requests.impl;

import com.google.gson.Gson;
import com.thedasmc.stocks2.requests.AbstractPlayerDataRequester;
import com.thedasmc.stocks2.requests.response.StockResponse;

import java.util.List;
import java.util.UUID;

public class PlayerDataRequester extends AbstractPlayerDataRequester {

    private static final String PORTFOLIO_URI = "/v1/player/portfolio";

    public PlayerDataRequester(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    @Override
    public List<StockResponse> getPortfolio(UUID uuid, int page) {
        return null;
    }

    @Override
    public StockResponse getStock(UUID uuid, String symbol) {
        return null;
    }
}
