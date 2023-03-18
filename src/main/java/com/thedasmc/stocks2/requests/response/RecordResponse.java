package com.thedasmc.stocks2.requests.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;

public class RecordResponse {

    private Long recordId;
    private UUID playerId;
    private String symbol;
    private Instant purchaseTime;
    private BigInteger cents;
    private BigDecimal shares;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Instant getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Instant purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public BigInteger getCents() {
        return cents;
    }

    public void setCents(BigInteger cents) {
        this.cents = cents;
    }

    public BigDecimal getShares() {
        return shares;
    }

    public void setShares(BigDecimal shares) {
        this.shares = shares;
    }
}
