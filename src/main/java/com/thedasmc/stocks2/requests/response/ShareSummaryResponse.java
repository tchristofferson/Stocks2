package com.thedasmc.stocks2.requests.response;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ShareSummaryResponse {

    private String symbol;
    private BigDecimal shares;
    private BigInteger centsValue;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getShares() {
        return shares;
    }

    public void setShares(BigDecimal shares) {
        this.shares = shares;
    }

    public BigInteger getCentsValue() {
        return centsValue;
    }

    public void setCentsValue(BigInteger centsValue) {
        this.centsValue = centsValue;
    }
}
