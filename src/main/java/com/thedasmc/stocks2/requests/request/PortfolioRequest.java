package com.thedasmc.stocks2.requests.request;

import java.util.UUID;

public class PortfolioRequest extends PageRequest {

    private final UUID uuid;

    public PortfolioRequest(String apiToken, int page, UUID uuid) {
        super(apiToken, page);
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
