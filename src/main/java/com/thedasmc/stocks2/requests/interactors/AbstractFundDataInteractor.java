package com.thedasmc.stocks2.requests.interactors;

import com.google.gson.Gson;
import com.thedasmc.stocks2.requests.request.*;
import com.thedasmc.stocks2.requests.response.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public abstract class AbstractFundDataInteractor extends DataInteractor {

    public AbstractFundDataInteractor(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    /**
     * Create a fund
     * @param request The request containing the fund's details you want to create
     * @return A {@link FundResponse} containing the fund's details
     * @throws IOException If an IO error occurs
     */
    public abstract FundResponse createFund(CreateFundRequest request) throws IOException;

    /**
     * Adds a stock to a fund
     * @param request The request containing the stock details you want to add to the fund
     * @return The {@link FundStockResponse} containing the details of the stock added
     * @throws IOException If an IO error occurs
     */
    public abstract FundStockResponse addStockToFund(FundStockRequest request) throws IOException;

    /**
     * Removes a stock from a fund
     * @param fundId The fund to remove the stock from
     * @param symbol The stock symbol to be removed from the fund
     * @throws IOException If an IO error occurs
     */
    public abstract void removeStockFromFund(long fundId, String symbol) throws IOException;

    /**
     * Updates a fund status
     * @param fundId The id of the fund to get the status updated
     * @param status The new status of the fund
     * @return A {@link FundResponse} containing the fund's details
     * @throws IOException If an IO error occurs
     */
    public abstract FundResponse updateFundStatus(long fundId, char status) throws IOException;

    /**
     * Buy/Sell shares of a fund.<br>
     * Buy if {@link FundTransactionRequest#getCents()} is greater than 0<br>
     * Sell if {@link FundTransactionRequest#getCents()} is less than 0
     * @param request The request containing the transaction details
     * @return A {@link FundRecordResponse} containing the transaction's details
     * @throws IOException If an IO error occurs
     */
    public abstract FundRecordResponse transactFund(FundTransactionRequest request) throws IOException;

    /**
     * Get popular funds
     * @param request The page details
     * @return A {@link List<FundResponse>} of the popular stocks where the most popular is index 0
     * @throws IOException If an IO error occurs
     */
    public abstract List<FundResponse> getPopularFunds(PageRequest request) throws IOException;

    /**
     * Get a fund by its fundId
     * @param fundId The id of the fund
     * @return A {@link FundResponse} with the specified fundId
     * @throws IOException If an IO error occurs
     */
    public abstract FundResponse getFund(long fundId) throws IOException;


    /**
     * Get the value of a player's investment in a fund
     * @param fundId The id of the fund
     * @param playerId The player's uuid to get the investment value for
     * @return a {@link FundResponse} containing the investment value
     * @throws IOException If an IO error occurs
     */
    public abstract FundValueResponse getPlayerFundInvestmentValue(long fundId, UUID playerId) throws IOException;

    /**
     * Get fund's created by the specified player
     * @param creatorId The player's uuid that created the funds
     * @return A list of fund's created by the specified player
     * @throws IOException If an IO error occurs
     */
    public abstract List<FundResponse> getFundsByCreator(UUID creatorId) throws IOException;

    /**
     * Get a player's portfolio of funds
     * @param playerId The player's uuid
     * @param page The page to fetch. Starts at 0
     * @return A {@link FundPortfolioResponse} containing the funds
     * @throws IOException If an IO error occurs
     */
    public abstract FundPortfolioResponse getFundPortfolio(UUID playerId, int page) throws IOException;

}
