package com.thedasmc.stocks2.requests.request;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

public class RecordRequest {

    private UUID playerId;
    private String symbol;
    private BigInteger cents;
    private BigDecimal shares;

    public RecordRequest() {
    }

    public RecordRequest(UUID playerId, String symbol, BigInteger cents, BigDecimal shares) {
        this.playerId = playerId;
        this.symbol = symbol;
        this.cents = cents;
        this.shares = shares;
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
