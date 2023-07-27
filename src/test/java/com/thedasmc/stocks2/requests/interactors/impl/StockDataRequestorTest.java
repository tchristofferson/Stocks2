package com.thedasmc.stocks2.requests.interactors.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thedasmc.stocks2.json.InstantConverter;
import com.thedasmc.stocks2.json.StockDataConverter;
import com.thedasmc.stocks2.requests.interactors.AbstractStockDataInteractor;
import com.thedasmc.stocks2.requests.interactors.impl.StockDataInteractor;
import com.thedasmc.stocks2.requests.response.StockDataResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StockDataRequestorTest {

    private static final String TOKEN = "cd07fac8c7434fa2987185e0f14e17ef";

    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(StockDataResponse.class, new StockDataConverter())
        .registerTypeAdapter(Instant.class, new InstantConverter())
        .create();

    @Test
    public void ensureNoErrorsWhenFetchingQuote() throws IOException {
        final String symbol = "AAPL";
        AbstractStockDataInteractor stockDataRequestor = new StockDataInteractor(TOKEN, gson);
        Map<String, StockDataResponse> response = stockDataRequestor.getQuotes(Collections.singletonList(symbol));

        assertNotNull(response);
        assertEquals(1, response.size());
        assertTrue(response.containsKey(symbol));
        assertNotNull(response.get(symbol));
    }

    @Test
    public void ensureCorrectResponseWithInvalidSymbol() {
        final String symbol = "AAPLLLL";
        AbstractStockDataInteractor stockDataRequestor = new StockDataInteractor(TOKEN, gson);
        IOException exception = assertThrows(IOException.class, () -> stockDataRequestor.getQuotes(Collections.singletonList(symbol)));

        assertEquals("invalid stock symbol!", exception.getMessage().toLowerCase());
    }
}