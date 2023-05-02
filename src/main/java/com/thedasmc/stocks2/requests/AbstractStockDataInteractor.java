package com.thedasmc.stocks2.requests;

import com.google.gson.Gson;
import com.thedasmc.stocks2.requests.response.StockDataResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractStockDataInteractor extends DataInteractor {

    public AbstractStockDataInteractor(String apiToken, Gson gson) {
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

    /**
     * Get the most popular stocks across all servers
     * @return A list of popular stocks
     *
     * @throws IOException If an error fetching popular stocks
     */
    public abstract List<StockDataResponse> getPopularStocks() throws IOException;
}
