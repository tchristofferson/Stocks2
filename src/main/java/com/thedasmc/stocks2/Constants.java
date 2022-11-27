package com.thedasmc.stocks2;

import com.google.gson.Gson;

public class Constants {

    private static Gson gson;

    public static Gson getGson() {
        return gson;
    }

    public static void setGson(Gson gson) {
        if (Constants.gson != null)
            throw new IllegalStateException("gson already set!");

        Constants.gson = gson;
    }
}
