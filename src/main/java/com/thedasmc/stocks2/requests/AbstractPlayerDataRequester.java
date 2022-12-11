package com.thedasmc.stocks2.requests;

import com.google.gson.Gson;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import com.thedasmc.stocks2.requests.response.StockResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

public abstract class AbstractPlayerDataRequester extends DataRequester {

    public AbstractPlayerDataRequester(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    /**
     * Get a player's portfolio.
     * @param uuid The player's UUID
     * @param page The portfolio page to fetch
     * @return A {@link PortfolioResponse} representing the player's portfolio for the specified page.
     * @throws IOException If an IO error occurs
     */
    public abstract PortfolioResponse getPortfolio(UUID uuid, int page) throws IOException;

    /**
     * Get a specific stock from the player's portfolio
     * @param uuid The player's UUID
     * @param symbol The stock symbol
     * @return A {@link StockResponse} representing the player's stock
     * @throws IOException If an IO error occurs
     */
    public abstract StockResponse getStock(UUID uuid, String symbol) throws IOException;

}
