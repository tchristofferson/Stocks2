package com.thedasmc.stocks2.commands.stocks;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.commands.AbstractStocksCommand;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.gui.GuiFactory;
import com.thedasmc.stocks2.requests.interactors.AbstractStockDataInteractor;
import com.thedasmc.stocks2.requests.response.StockDataResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@CommandAlias("stocks")
public class PopularCommand extends AbstractStocksCommand {

    private static final String POPULAR_PERMISSION = "stocks.popular";

    public PopularCommand(Stocks2 plugin) {
        super(plugin);
    }

    @Subcommand("popular|pop")
    @CommandPermission(POPULAR_PERMISSION)
    @Description("Get the most popular stocks across all servers")
    public void onPopular(Player player) {
        final Texts texts = plugin.getTexts();
        final UUID uuid = player.getUniqueId();
        final AbstractStockDataInteractor stockDataInteractor = plugin.getStockDataInteractor();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            List<StockDataResponse> popularStocks;

            try {
                popularStocks = stockDataInteractor.getPopularStocks();
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.POPULAR_STOCKS_ERROR, e.getMessage()));
                return;
            }

            final Inventory popularStocksInventory = GuiFactory.createStockPage(ChatColor.GOLD + "Popular Stocks", popularStocks, texts);
            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.getPopularTracker().track(uuid, popularStocksInventory);
                player.openInventory(popularStocksInventory);
            });
        });
    }

}
