package com.thedasmc.stocks2.requests.response;

import java.util.List;
import java.util.UUID;

public class FundResponse {

    private Long fundId;
    private UUID creatorId;
    private Long serverId;
    private String name;
    private Character status;
    private Long created;
    private List<StockDataResponse> stocks;

    public Long getFundId() {
        return fundId;
    }

    public void setFundId(Long fundId) {
        this.fundId = fundId;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public List<StockDataResponse> getStocks() {
        return stocks;
    }

    public void setStocks(List<StockDataResponse> stocks) {
        this.stocks = stocks;
    }
}
