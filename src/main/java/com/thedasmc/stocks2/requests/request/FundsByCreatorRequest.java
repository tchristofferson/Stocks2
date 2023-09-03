package com.thedasmc.stocks2.requests.request;

import java.util.UUID;

import static com.thedasmc.stocks2.common.Constants.STOCK_GUI_MAX;

public class FundsByCreatorRequest extends PageRequest {

    private final UUID creatorId;

    public FundsByCreatorRequest(UUID creatorId, int page) {
        super(page, STOCK_GUI_MAX);
        this.creatorId = creatorId;
    }

    public UUID getCreatorId() {
        return creatorId;
    }
}
