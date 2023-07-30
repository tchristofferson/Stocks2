package com.thedasmc.stocks2;

import co.aikar.commands.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tchristofferson.configupdater.ConfigUpdater;
import com.thedasmc.stocks2.commands.*;
import com.thedasmc.stocks2.common.Constants;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.gui.GuiTracker;
import com.thedasmc.stocks2.gui.PortfolioViewer;
import com.thedasmc.stocks2.json.InstantConverter;
import com.thedasmc.stocks2.json.StockDataConverter;
import com.thedasmc.stocks2.listeners.InventoryListener;
import com.thedasmc.stocks2.logging.Log4JFilter;
import com.thedasmc.stocks2.requests.interactors.*;
import com.thedasmc.stocks2.requests.interactors.impl.*;
import com.thedasmc.stocks2.requests.response.StockDataResponse;
import net.milkbowl.vault.economy.Economy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Stocks2 extends JavaPlugin {

    private Gson gson;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final GuiTracker<UUID, PortfolioViewer> portfolioTracker = new GuiTracker<>();
    private final GuiTracker<UUID, Inventory> popularTracker = new GuiTracker<>();
    private AbstractStockDataInteractor stockDataInteractor;
    private AbstractPlayerDataInteractor playerDataInteractor;
    private AbstractAccountInteractor accountInteractor;
    private AbstractServerInteractor serverInteractor;
    private AbstractFundDataInteractor fundDataInteractor;
    private FileConfiguration textsConfig;
    private Texts texts;
    private Economy economy;

    @Override
    public void onEnable() {
        saveResource("texts.yml", false);
        saveDefaultConfig();

        updateTexts();
        updateConfig();

        final String apiToken = getConfig().getString("api-token");

        if (apiToken == null || apiToken.replace(" ", "").isEmpty())
            Bukkit.getLogger().warning("No api-token specified in config! Use the register commands to register and account and server.");

        if (!initEconomy()) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found! Make sure you installed an economy plugin as well as Vault.", getDescription().getName()));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        initLogFilter();
        initGson();
        initInteractors(apiToken);
        initCommandManager();
        initTextsConfig();
        initTexts();

        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.executorService.shutdown();
    }

    public Gson getGson() {
        return gson;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public GuiTracker<UUID, PortfolioViewer> getPortfolioTracker() {
        return portfolioTracker;
    }

    public GuiTracker<UUID, Inventory> getPopularTracker() {
        return popularTracker;
    }

    public AbstractStockDataInteractor getStockDataInteractor() {
        return stockDataInteractor;
    }

    public AbstractPlayerDataInteractor getPlayerDataInteractor() {
        return playerDataInteractor;
    }

    public AbstractAccountInteractor getAccountInteractor() {
        return accountInteractor;
    }

    public AbstractServerInteractor getServerInteractor() {
        return serverInteractor;
    }

    public AbstractFundDataInteractor getFundDataInteractor() {
        return fundDataInteractor;
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

    public Duration getTradeCooldownDuration() {
        return Duration.ofSeconds(getConfig().getLong("trade-cooldown", 900));
    }

    public boolean isStockWhitelistEnabled() {
        return getConfig().getBoolean("whitelist.enabled", false);
    }

    public boolean isStockBlacklistEnabled() {
        //Whitelist takes precedent over blacklist
        return getConfig().getBoolean("blacklist.enabled", false) && !isStockWhitelistEnabled();
    }

    public List<String> getWhitelist() {
        return getConfig().getStringList("whitelist.stocks");
    }

    public List<String> getBlacklist() {
        return getConfig().getStringList("blacklist.stocks");
    }

    //Use isStockBlacklistEnabled() in tandem
    public boolean isBlacklisted(String symbol) {
        return getBlacklist().stream()
            .anyMatch(s -> s.trim().equalsIgnoreCase(symbol));
    }

    //use isStockWhitelistEnabled in tandem
    public boolean isWhitelisted(String symbol) {
        return getWhitelist().stream()
            .anyMatch(s -> s.trim().equalsIgnoreCase(symbol));
    }

    private void initGson() {
        gson = new GsonBuilder()
            .registerTypeAdapter(StockDataResponse.class, new StockDataConverter())
            .registerTypeAdapter(Instant.class, new InstantConverter())
            .create();
    }

    private void initInteractors(String apiToken) {
        stockDataInteractor = new StockDataInteractor(apiToken, gson);
        playerDataInteractor = new PlayerDataInteractor(apiToken, gson);
        accountInteractor = new AccountInteractor(apiToken, gson);
        serverInteractor = new ServerInteractor(apiToken, gson);
        fundDataInteractor = new FundDataInteractor(apiToken, gson);
    }

    private void initCommandManager() {
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");

        CommandConditions<BukkitCommandIssuer, BukkitCommandExecutionContext, BukkitConditionContext> commandConditions = commandManager.getCommandConditions();
        commandConditions.addCondition(Double.class, Constants.POSITIVE_SHARE_LIMITS_CONDITION, (c, exec, value) -> {
            if (value <= 0)
                throw new ConditionFailedException(texts.getText(Texts.Types.GIVE_SHARES_TOO_LOW));
        });

        commandManager.registerCommand(new StocksHelpCommand());
        commandManager.registerCommand(new PortfolioCommand(this));
        commandManager.registerCommand(new SellCommand(this));
        commandManager.registerCommand(new SellAllCommand(this));
        commandManager.registerCommand(new BuyCommand(this));
        commandManager.registerCommand(new CheckCommand(this));
        commandManager.registerCommand(new RegisterAccountCommand(this));
        commandManager.registerCommand(new RegisterServerCommand(this));
        commandManager.registerCommand(new GiveCommand(this));
        commandManager.registerCommand(new TakeCommand(this));
        commandManager.registerCommand(new PopularCommand(this));
        commandManager.registerCommand(new CreateFundCommand(this));
        commandManager.registerCommand(new AddStockToFundCommand(this));
        commandManager.registerCommand(new RemoveStockFromFundCommand(this));
        commandManager.registerCommand(new PublishFundCommand(this));
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

    private void initLogFilter() {
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addFilter(new Log4JFilter());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);
    }

    private void updateConfig() {
        try {
            ConfigUpdater.update(this, "config.yml", new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to update config.yml!");
            throw new RuntimeException(e);
        }
    }

    private void updateTexts() {
        try {
            ConfigUpdater.update(this, "texts.yml", new File(getDataFolder(), "texts.yml"));
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to update texts.yml!");
            throw new RuntimeException(e);
        }
    }
}
