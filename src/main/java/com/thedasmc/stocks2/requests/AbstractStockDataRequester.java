package com.thedasmc.stocks2.requests;

import com.thedasmc.stocks2.requests.models.StockData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractStockDataRequester {

    protected final List<String> symbols;
    protected final String token;

    protected AbstractStockDataRequester(List<String> symbols, String token) {
        this.symbols = symbols;
        this.token = token;
    }

    public List<String> getSymbols() {
        return new ArrayList<>(symbols);
    }

    public String getToken() {
        return token;
    }

    /**
     * Get quotes for the stocks for this instance
     *
     * @return A map where the key is the stock symbol (UpperCase) and the value is the {@link StockData} representing the quote.
     * If there was an error finding a symbol the value should be null.
     *
     * @throws IOException If an error occurs fetching the quotes.
     */
    public abstract Map<String, StockData> getQuotes() throws IOException;
}
