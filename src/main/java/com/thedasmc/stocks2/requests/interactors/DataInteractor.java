package com.thedasmc.stocks2.requests.interactors;

import com.google.gson.Gson;

public class DataInteractor {

    protected final String apiToken;
    protected final Gson gson;

    public DataInteractor(String apiToken, Gson gson) {
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
