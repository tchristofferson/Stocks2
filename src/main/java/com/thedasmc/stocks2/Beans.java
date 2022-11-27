package com.thedasmc.stocks2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thedasmc.stocks2.json.StockDataConverter;
import com.thedasmc.stocks2.requests.models.StockData;

public class Beans {

    private volatile static Gson gson;

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                .registerTypeAdapter(StockData.class, new StockDataConverter())
                .create();
        }

        return gson;
    }
}
