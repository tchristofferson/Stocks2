package com.thedasmc.stocks2.requests;

import com.google.gson.Gson;
import com.thedasmc.stocks2.requests.response.StockDataResponse;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public abstract class AbstractStockDataRequestor extends DataRequester {

    public AbstractStockDataRequestor(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    /**
     * Get quotes for the stocks for this instance
     * @param symbols Stock symbols to fetch
     * @return A map where the key is the stock symbol (UpperCase) and the value is the {@link StockDataResponse} representing the quote.
     * If the symbol is invalid the value should be null.
     *
     * @throws IOException If an error occurs fetching the quotes.
     */
    public abstract Map<String, StockDataResponse> getQuotes(Collection<String> symbols) throws IOException;
}
