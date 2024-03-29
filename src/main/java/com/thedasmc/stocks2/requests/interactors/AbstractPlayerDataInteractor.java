package com.thedasmc.stocks2.requests.interactors;

import com.google.gson.Gson;
import com.thedasmc.stocks2.requests.request.DeleteRecordsRequest;
import com.thedasmc.stocks2.requests.request.RecordRequest;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import com.thedasmc.stocks2.requests.response.RecordResponse;
import com.thedasmc.stocks2.requests.response.ShareSummaryResponse;
import com.thedasmc.stocks2.requests.response.StockResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public abstract class AbstractPlayerDataInteractor extends DataInteractor {

    public AbstractPlayerDataInteractor(String apiToken, Gson gson) {
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
     * @return A {@link StockResponse} representing the player's stock, or {@code null} if the symbol is invalid
     * @throws IOException If an IO error occurs
     */
    public abstract StockResponse getStock(UUID uuid, String symbol) throws IOException;

    /**
     * Add a record. A record is added when buying, selling, giving, and taking shares
     * @param recordRequest The request
     * @return A {@link RecordResponse} of the inserted record
     * @throws IOException If an IO error occurs
     */
    public abstract RecordResponse transact(RecordRequest recordRequest) throws IOException;

    /**
     * Get the player's ENTIRE portfolio which includes symbol, shares, and value
     * @param playerId The player's UUID
     * @return A list of {@link ShareSummaryResponse}'s representing the player's portfolio
     * @throws IOException If an IO error occurs
     */
    public abstract List<ShareSummaryResponse> getShareSummaries(UUID playerId) throws IOException;

    /**
     * Delete the specified number of shares for the specified stocks.
     * @param request The request
     * @return {@code true} if successful, otherwise {@code false}
     * @throws IOException If an IO error occurs
     */
    public abstract boolean deleteRecords(DeleteRecordsRequest request) throws IOException;

}
