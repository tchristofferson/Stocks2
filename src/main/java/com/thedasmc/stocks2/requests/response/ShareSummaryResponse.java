package com.thedasmc.stocks2.requests.response;

import com.thedasmc.stocks2.common.Cooldownable;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ShareSummaryResponse implements Cooldownable {

    private String symbol;
    private BigDecimal shares;
    private BigInteger centsValue;
    private Long lastPurchaseTime;

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

    @Override
    public Long getLastPurchaseTime() {
        return lastPurchaseTime;
    }

    public void setLastPurchaseTime(Long lastPurchaseTime) {
        this.lastPurchaseTime = lastPurchaseTime;
    }
}
