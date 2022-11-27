package com.thedasmc.stocks2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thedasmc.stocks2.json.StockDataConverter;
import com.thedasmc.stocks2.requests.models.StockData;
import org.bukkit.plugin.java.JavaPlugin;

public final class Stocks2 extends JavaPlugin {

    @Override
    public void onEnable() {
        Constants.setGson(
            new GsonBuilder()
                .registerTypeAdapter(StockData.class, new StockDataConverter())
                .create()
        );


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
