package com.thedasmc.stocks2.requests.request;

import java.util.UUID;

import static com.thedasmc.stocks2.common.Constants.STOCK_GUI_MAX;

public class FundPortfolioRequest extends PageRequest {

    private final UUID playerId;

    public FundPortfolioRequest(UUID playerId, int page) {
        super(page, STOCK_GUI_MAX);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

}
