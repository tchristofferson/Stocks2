package com.thedasmc.stocks2;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thedasmc.stocks2.commands.PortfolioCommand;
import com.thedasmc.stocks2.commands.SellCommand;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.core.PortfolioTracker;
import com.thedasmc.stocks2.json.LocalDateTimeConverter;
import com.thedasmc.stocks2.json.StockDataConverter;
import com.thedasmc.stocks2.listeners.InventoryListener;
import com.thedasmc.stocks2.requests.AbstractPlayerDataInteractor;
import com.thedasmc.stocks2.requests.AbstractStockDataRequestor;
import com.thedasmc.stocks2.requests.impl.PlayerDataInteractor;
import com.thedasmc.stocks2.requests.impl.StockDataRequestor;
import com.thedasmc.stocks2.requests.request.RecordRequest;
import com.thedasmc.stocks2.requests.response.RecordResponse;
import com.thedasmc.stocks2.requests.response.StockDataResponse;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.time.LocalDateTime;

public final class Stocks2 extends JavaPlugin {

    private Gson gson;
    private AbstractStockDataRequestor stockDataRequester;
    private AbstractPlayerDataInteractor playerDataRequester;
    private PaperCommandManager commandManager;
    private PortfolioTracker portfolioTracker;
    private FileConfiguration textsConfig;
    private Texts texts;
    private Economy economy;

    @Override
    public void onEnable() {
        saveResource("texts.yml", false);
        saveDefaultConfig();
        final String apiToken = getConfig().getString("api-token");

        if (apiToken == null || apiToken.replace(" ", "").isEmpty()) {
            Bukkit.getLogger().warning("No api-token specified in config! Disabling . . .");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!initEconomy()) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found! Make sure you installed an economy plugin as well as Vault.", getDescription().getName()));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        initGson();
        initStockDataRequester(apiToken);
        initPlayerDataRequester(apiToken);
        initCommandManager();
        initPortfolioTracker();
        initTextsConfig();
        initTexts();

        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Gson getGson() {
        return gson;
    }

    public AbstractStockDataRequestor getStockDataRequester() {
        return stockDataRequester;
    }

    public AbstractPlayerDataInteractor getPlayerDataRequester() {
        return playerDataRequester;
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }

    public PortfolioTracker getPortfolioTracker() {
        return portfolioTracker;
    }

    public FileConfiguration getTextsConfig() {
        return textsConfig;
    }

    public Texts getTexts() {
        return texts;
    }

    public Economy getEconomy() {
        return economy;
    }

    private void initGson() {
        gson = new GsonBuilder()
            .registerTypeAdapter(StockDataResponse.class, new StockDataConverter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
            .create();
    }

    private void initStockDataRequester(String apiToken) {
        stockDataRequester = new StockDataRequestor(apiToken, gson);
    }

    private void initPlayerDataRequester(String apiToken) {
        playerDataRequester = new PlayerDataInteractor(apiToken, gson);
    }

    private void initCommandManager() {
        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new PortfolioCommand(this));
        commandManager.registerCommand(new SellCommand(this));
    }

    private void initPortfolioTracker() {
        portfolioTracker = new PortfolioTracker();
    }

    private void initTextsConfig() {
        File file = new File(getDataFolder(), "texts.yml");
        textsConfig = YamlConfiguration.loadConfiguration(file);
    }

    private void initTexts() {
        texts = new Texts(this);
    }

    private boolean initEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null)
            return false;

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (rsp == null)
            return false;

        economy = rsp.getProvider();
        return economy != null;
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);
    }
}
