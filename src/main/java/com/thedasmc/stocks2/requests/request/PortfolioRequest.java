package com.thedasmc.stocks2.requests.request;

import java.util.UUID;

import static com.thedasmc.stocks2.common.Constants.PORTFOLIO_STOCKS;

public class PortfolioRequest extends PageRequest {

    private final UUID playerId;

    public PortfolioRequest(String apiToken, int page, UUID playerId) {
        super(apiToken, page, PORTFOLIO_STOCKS);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }
}
