package com.thedasmc.stocks2.requests.interactors.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thedasmc.stocks2.json.InstantConverter;
import com.thedasmc.stocks2.json.StockDataConverter;
import com.thedasmc.stocks2.requests.request.RecordRequest;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import com.thedasmc.stocks2.requests.response.RecordResponse;
import com.thedasmc.stocks2.requests.response.StockDataResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;

import static com.thedasmc.stocks2.requests.interactors.impl.TestConstants.TEST_TOKEN;

public class PlayerDataInteractorTest {

    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(StockDataResponse.class, new StockDataConverter())
        .registerTypeAdapter(Instant.class, new InstantConverter())
        .create();

    @Test
    public void ensureNoErrorsWhenFetchingPortfolio() throws IOException {
        PlayerDataInteractor playerDataInteractor = new PlayerDataInteractor(TEST_TOKEN, gson);
        PortfolioResponse response = playerDataInteractor.getPortfolio(UUID.fromString("7c8fa59e-4999-4da0-bb57-faf78e7cbf84"), 0);
        System.out.println("Page: " + response.getPage());
        System.out.println("Pages: " + response.getPages());
    }

    @Test
    public void ensureNoErrorsWhenBuying() throws IOException {
        PlayerDataInteractor playerDataInteractor = new PlayerDataInteractor(TEST_TOKEN, gson);
        RecordRequest recordRequest = new RecordRequest(UUID.fromString("b5a3e507-e849-4f1e-86d6-1b2b5b6f0d78"), "AAPL", BigInteger.valueOf(10000), BigDecimal.ONE);
        RecordResponse response = playerDataInteractor.transact(recordRequest);
        System.out.println("RecordId: " + response.getRecordId());
    }
}