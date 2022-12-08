package com.thedasmc.stocks2.requests;

import com.google.gson.Gson;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;

import java.io.IOException;
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
     */
    public abstract PortfolioResponse getPortfolio(UUID uuid, int page) throws IOException;

}
