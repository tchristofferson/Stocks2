package com.thedasmc.stocks2.requests;

import com.google.gson.Gson;
import com.thedasmc.stocks2.requests.response.StockResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public abstract class AbstractPlayerDataRequester extends DataRequester {

    public AbstractPlayerDataRequester(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    /**
     * Get a player's portfolio.
     * @param uuid The player's UUID
     * @param page The portfolio page to fetch
     * @return A list of {@link StockResponse}s representing the player's portfolio for the specified page.
     */
    public abstract List<StockResponse> getPortfolio(UUID uuid, int page) throws IOException;

}
