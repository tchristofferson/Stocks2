package com.thedasmc.stocks2;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thedasmc.stocks2.commands.PortfolioCommand;
import com.thedasmc.stocks2.core.PortfolioTracker;
import com.thedasmc.stocks2.json.StockDataConverter;
import com.thedasmc.stocks2.requests.AbstractPlayerDataRequester;
import com.thedasmc.stocks2.requests.AbstractStockDataRequester;
import com.thedasmc.stocks2.requests.impl.PlayerDataRequester;
import com.thedasmc.stocks2.requests.impl.StockDataRequester;
import com.thedasmc.stocks2.requests.response.StockDataResponse;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Stocks2 extends JavaPlugin {

    private Gson gson;
    private AbstractStockDataRequester stockDataRequester;
    private AbstractPlayerDataRequester playerDataRequester;
    private PaperCommandManager commandManager;
    private PortfolioTracker portfolioTracker;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        final String apiToken = getConfig().getString("api-token");

        if (apiToken == null || apiToken.replace(" ", "").isEmpty()) {
            Bukkit.getLogger().warning("No api-token specified in config! Disabling . . .");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        initGson();
        initStockDataRequester(apiToken);
        initPlayerDataRequester(apiToken);
        initCommandManager();
        initPortfolioTracker();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Gson getGson() {
        return gson;
    }

    public AbstractStockDataRequester getStockDataRequester() {
        return stockDataRequester;
    }

    public AbstractPlayerDataRequester getPlayerDataRequester() {
        return playerDataRequester;
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }

    public PortfolioTracker getPortfolioTracker() {
        return portfolioTracker;
    }

    private void initGson() {
        gson = new GsonBuilder()
            .registerTypeAdapter(StockDataResponse.class, new StockDataConverter())
            .create();
    }

    private void initStockDataRequester(String apiToken) {
        stockDataRequester = new StockDataRequester(apiToken, gson);
    }

    private void initPlayerDataRequester(String apiToken) {
        playerDataRequester = new PlayerDataRequester(apiToken, gson);
    }

    private void initCommandManager() {
        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new PortfolioCommand(this));
    }

    private void initPortfolioTracker() {
        portfolioTracker = new PortfolioTracker();
    }
}
