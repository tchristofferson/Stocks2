package com.thedasmc.stocks2.requests.request;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class DeleteRecordsRequest extends BaseRequest {

    private UUID playerId;
    private Map<String, BigDecimal> stocks;

    public DeleteRecordsRequest(UUID playerId, Map<String, BigDecimal> stocks) {
        this.playerId = playerId;
        this.stocks = stocks;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public Map<String, BigDecimal> getStocks() {
        return stocks;
    }

    public void setStocks(Map<String, BigDecimal> stocks) {
        this.stocks = stocks;
    }
}
