package com.thedasmc.stocks2.requests.request;

import java.util.UUID;

public class FundTransactionRequest {

    private Long fundId;
    private UUID playerId;
    private Long cents;

    public Long getFundId() {
        return fundId;
    }

    public void setFundId(Long fundId) {
        this.fundId = fundId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public Long getCents() {
        return cents;
    }

    public void setCents(Long cents) {
        this.cents = cents;
    }
}
