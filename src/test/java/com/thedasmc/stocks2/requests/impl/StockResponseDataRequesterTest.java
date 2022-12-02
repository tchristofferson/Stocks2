package com.thedasmc.stocks2.requests.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thedasmc.stocks2.json.StockDataConverter;
import com.thedasmc.stocks2.requests.response.StockDataResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

public class StockResponseDataRequesterTest {

    private final Gson gson = new GsonBuilder()
        .registerTypeAdapter(StockDataResponse.class, new StockDataConverter())
        .create();

    @Test
    void name() throws IOException {
        PlayerDataRequester playerDataRequester = new PlayerDataRequester("cd07fac8c7434fa2987185e0f14e17ef", gson);
        playerDataRequester.getPortfolio(UUID.fromString("7c8fa59e-4999-4da0-bb57-faf78e7cbf84"), 0);
    }
}