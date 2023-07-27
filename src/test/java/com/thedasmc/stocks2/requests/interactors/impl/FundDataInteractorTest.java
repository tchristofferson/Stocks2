package com.thedasmc.stocks2.requests.interactors.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thedasmc.stocks2.requests.FundStock;
import com.thedasmc.stocks2.requests.interactors.AbstractFundDataInteractor;
import com.thedasmc.stocks2.requests.request.CreateFundRequest;
import com.thedasmc.stocks2.requests.request.FundTransactionRequest;
import com.thedasmc.stocks2.requests.request.PageRequest;
import com.thedasmc.stocks2.requests.response.FundRecordResponse;
import com.thedasmc.stocks2.requests.response.FundResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static com.thedasmc.stocks2.requests.interactors.impl.TestConstants.TEST_TOKEN;

public class FundDataInteractorTest {

    private static final String TEST_FUND_NAME = "FundDataInteractorTest Fund";

    private final Gson gson = new GsonBuilder().create();
    private final AbstractFundDataInteractor fundDataInteractor = new FundDataInteractor(TEST_TOKEN, gson);

    @Test
    public void testCreateFund() throws IOException {
        createFund();
    }

    @Test
    public void testAddStockToFund() throws IOException {
        createFundWithStocks("AAPL");
    }

    @Test
    public void testRemoveStockFromFund() throws IOException {
        String symbol = "aapl";
        FundResponse createFundResponse = createFundWithStocks(symbol);
        fundDataInteractor.removeStockFromFund(createFundResponse.getFundId(), symbol);
    }

    @Test
    public void testUpdateFundStatus() throws IOException {
        createOpenFund("AAPL", "TSLA");
    }

    @Test
    public void testTransactFund() throws IOException {
        FundResponse createFundResponse = createOpenFund("AAPL", "TSLA");
        transactFund(createFundResponse.getFundId());
    }

    @Test
    public void testGetPopularFunds() throws IOException {
        FundResponse createFundResponse = createOpenFund("AAPL", "TSLA");
        transactFund(createFundResponse.getFundId());
        createOpenFund("GOOG", "AMZN");

        PageRequest pageRequest = new PageRequest(0, 10);
        fundDataInteractor.getPopularFunds(pageRequest);
    }

    private FundResponse createFund() throws IOException {
        CreateFundRequest request = getCreateFundRequest();
        return fundDataInteractor.createFund(request);
    }

    private FundResponse createFundWithStocks(String... symbols) throws IOException {
        FundResponse createFundResponse = createFund();

        for (String symbol : symbols) {
            FundStock addStockRequest = new FundStock();
            addStockRequest.setFundId(createFundResponse.getFundId());
            addStockRequest.setSymbol(symbol);

            fundDataInteractor.addStockToFund(addStockRequest);
        }

        return createFundResponse;
    }

    private FundResponse createOpenFund(String... symbols) throws IOException {
        FundResponse fundResponse = createFundWithStocks(symbols);
        fundDataInteractor.updateFundStatus(fundResponse.getFundId(), 'o');

        return fundResponse;
    }

    private FundRecordResponse transactFund(long fundId) throws IOException {
        FundTransactionRequest fundTransactionRequest = new FundTransactionRequest();
        fundTransactionRequest.setFundId(fundId);
        fundTransactionRequest.setPlayerId(UUID.randomUUID());
        fundTransactionRequest.setCents(10000L);

        return fundDataInteractor.transactFund(fundTransactionRequest);
    }

    private CreateFundRequest getCreateFundRequest() {
        CreateFundRequest request = new CreateFundRequest();
        request.setCreatorId(UUID.randomUUID());
        request.setName(TEST_FUND_NAME);

        return request;
    }
}