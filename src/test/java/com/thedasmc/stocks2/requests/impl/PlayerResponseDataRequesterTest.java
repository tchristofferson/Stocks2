package com.thedasmc.stocks2.requests.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thedasmc.stocks2.json.StockDataConverter;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import com.thedasmc.stocks2.requests.response.StockDataResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

public class PlayerResponseDataRequesterTest {

    private static final String TOKEN = "cd07fac8c7434fa2987185e0f14e17ef";

    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(StockDataResponse.class, new StockDataConverter())
        .create();

    @Test
    public void ensureNoErrorsWhenFetchingPortfolio() throws IOException {
        PlayerDataRequester playerDataRequester = new PlayerDataRequester(TOKEN, gson);
        PortfolioResponse response = playerDataRequester.getPortfolio(UUID.fromString("7c8fa59e-4999-4da0-bb57-faf78e7cbf84"), 0);
        System.out.println("Page: " + response.getPage());
        System.out.println("Pages: " + response.getPages());
    }
}