package com.thedasmc.stocks2.requests.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thedasmc.stocks2.json.LocalDateTimeConverter;
import com.thedasmc.stocks2.json.StockDataConverter;
import com.thedasmc.stocks2.requests.request.RecordRequest;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import com.thedasmc.stocks2.requests.response.RecordResponse;
import com.thedasmc.stocks2.requests.response.StockDataResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerResponseDataRequesterTest {

    private static final String TOKEN = "cd07fac8c7434fa2987185e0f14e17ef";

    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(StockDataResponse.class, new StockDataConverter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
        .create();

    @Test
    public void ensureNoErrorsWhenFetchingPortfolio() throws IOException {
        PlayerDataInteractor playerDataInteractor = new PlayerDataInteractor(TOKEN, gson);
        PortfolioResponse response = playerDataInteractor.getPortfolio(UUID.fromString("7c8fa59e-4999-4da0-bb57-faf78e7cbf84"), 0);
        System.out.println("Page: " + response.getPage());
        System.out.println("Pages: " + response.getPages());
    }

    @Test
    public void ensureNoErrorsWhenSelling() throws IOException {
        PlayerDataInteractor playerDataInteractor = new PlayerDataInteractor(TOKEN, gson);
        RecordRequest recordRequest = new RecordRequest(UUID.fromString("b5a3e507-e849-4f1e-86d6-1b2b5b6f0d78"), "AAPL", BigInteger.valueOf(10000), BigDecimal.ONE);
        RecordResponse response = playerDataInteractor.transact(recordRequest);
        System.out.println("RecordId: " + response.getRecordId());
    }
}