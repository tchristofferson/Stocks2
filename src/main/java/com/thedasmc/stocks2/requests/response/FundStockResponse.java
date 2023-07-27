package com.thedasmc.stocks2.requests.response;

public class FundStockResponse {

    private Long fundId;
    private String symbol;

    public Long getFundId() {
        return fundId;
    }

    public void setFundId(Long fundId) {
        this.fundId = fundId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

}
