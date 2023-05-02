package com.thedasmc.stocks2.requests.request;

import java.util.UUID;

import static com.thedasmc.stocks2.common.Constants.STOCK_GUI_MAX;

public class PortfolioRequest extends PageRequest {

    private final UUID playerId;

    public PortfolioRequest(String apiToken, int page, UUID playerId) {
        super(apiToken, page, STOCK_GUI_MAX);
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }
}
