package com.thedasmc.stocks2.requests;

import com.google.gson.Gson;

public class DataRequester {

    protected final String apiToken;
    protected final Gson gson;

    public DataRequester(String apiToken, Gson gson) {
        this.apiToken = apiToken;
        this.gson = gson;
    }

    public String getApiToken() {
        return apiToken;
    }

    public Gson getGson() {
        return gson;
    }
}
