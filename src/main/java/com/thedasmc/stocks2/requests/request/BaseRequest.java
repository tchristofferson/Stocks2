package com.thedasmc.stocks2.requests.request;

public class BaseRequest {

    private final String apiToken;

    public BaseRequest(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getApiToken() {
        return apiToken;
    }
}
