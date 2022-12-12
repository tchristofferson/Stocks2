package com.thedasmc.stocks2.requests.request;

public class BaseRequest {

    private String apiToken;

    public BaseRequest() {
    }

    public BaseRequest(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}
