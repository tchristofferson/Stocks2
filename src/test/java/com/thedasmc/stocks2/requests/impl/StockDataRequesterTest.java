package com.thedasmc.stocks2.requests.impl;

import com.thedasmc.stocks2.requests.AbstractStockDataRequester;
import com.thedasmc.stocks2.requests.models.StockData;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StockDataRequesterTest {

    @Test
    public void testGetQuotes() throws IOException {
        AbstractStockDataRequester requester = new StockDataRequester(Collections.singletonList("AAPL"), "cd07fac8c7434fa2987185e0f14e17ef");
        Map<String, StockData> stockDataMap = requester.getQuotes();
        assertEquals(stockDataMap.size(), 1);
        assertTrue(stockDataMap.containsKey("AAPL"));
        assertNotNull(stockDataMap.get("AAPL"));
    }
}