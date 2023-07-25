package com.thedasmc.stocks2.requests.request;

import java.util.UUID;

public class CreateFundRequest extends BaseRequest {

    private UUID creatorId;
    private String name;

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
