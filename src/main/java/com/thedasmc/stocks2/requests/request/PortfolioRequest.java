package com.thedasmc.stocks2.requests.request;

import java.util.UUID;

public class PortfolioRequest extends PageRequest {

    private final UUID playerId;

    public PortfolioRequest(String apiToken, int page, UUID playerId) {
        super(apiToken, page, 45);//54 is max inventory size. Subtract bottom row for navigation, the most slots is now 45
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }
}
