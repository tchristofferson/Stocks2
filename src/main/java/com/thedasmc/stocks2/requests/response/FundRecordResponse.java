package com.thedasmc.stocks2.requests.response;

import java.util.UUID;

public class FundRecordResponse {

    private Long fundRecordId;
    private Long fundId;
    private UUID playerId;
    private Long serverId;
    private Long centsInvested;
    private Long centsCurrentFundValue;

    public Long getFundRecordId() {
        return fundRecordId;
    }

    public void setFundRecordId(Long fundRecordId) {
        this.fundRecordId = fundRecordId;
    }

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

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public Long getCentsInvested() {
        return centsInvested;
    }

    public void setCentsInvested(Long centsInvested) {
        this.centsInvested = centsInvested;
    }

    public Long getCentsCurrentFundValue() {
        return centsCurrentFundValue;
    }

    public void setCentsCurrentFundValue(Long centsCurrentFundValue) {
        this.centsCurrentFundValue = centsCurrentFundValue;
    }
}
