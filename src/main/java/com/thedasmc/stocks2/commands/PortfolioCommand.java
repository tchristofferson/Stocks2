package com.thedasmc.stocks2.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.core.PortfolioViewer;
import com.thedasmc.stocks2.requests.AbstractPlayerDataRequester;
import com.thedasmc.stocks2.requests.response.StockResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

@CommandAlias("stocks")
public class PortfolioCommand extends BaseCommand {

    private static final String PORTFOLIO_PERMISSION = "stocks.portfolio";
    private static final String PORTFOLIO_OTHERS_PERMISSION = "stocks.portfolio.others";

    private final Stocks2 plugin;

    public PortfolioCommand(Stocks2 plugin) {
        this.plugin = plugin;
    }

    @Subcommand("portfolio")
    @CommandPermission(PORTFOLIO_PERMISSION)
    @Description("View your portfolio")
    public void onPortfolio(Player player) {
        AbstractPlayerDataRequester playerDataRequester = plugin.getPlayerDataRequester();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            List<StockResponse> stocks;

            try {
                stocks = playerDataRequester.getPortfolio(player.getUniqueId(), 0);
            } catch (IOException e) {
                player.sendMessage(ChatColor.RED + "[Stocks]There was an error fetching your portfolio: " + e.getMessage() + "!");
                return;
            }

            Inventory portfolio = createPortfolio(stocks);

            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.getPortfolioTracker().track(new PortfolioViewer(player.getUniqueId(), player.getUniqueId(), 0));
                player.openInventory(portfolio);
            });
        });
    }

    @Subcommand("portfolio")
    @CommandPermission(PORTFOLIO_OTHERS_PERMISSION)
    @Description("View another player's portfolio")
    public void onOtherPortfolio(Player player, OnlinePlayer other) {

    }

    private Inventory createPortfolio(List<StockResponse> stocks) {
        //Only 45 per page, bottom row reserved for navigation
        stocks = stocks.subList(0, Math.min(45, stocks.size()));
        Inventory portfolio = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Stock Portfolio");
        stocks.forEach(stock -> portfolio.addItem(getStockItem(stock)));

        //TODO: Add navigation items

        return portfolio;
    }

    private ItemStack getStockItem(StockResponse stock) {
        ItemStack itemStack = new ItemStack(Material.EMERALD, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + stock.getCompanyName());

        itemMeta.setLore(Arrays.asList(
            ChatColor.GREEN + "Shares: " + stock.getShares().toPlainString(),
            ChatColor.GREEN + "Invested: " + new BigDecimal(stock.getCentsInvested()).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN).toPlainString(),
            ChatColor.GREEN + "Value: " + stock.getShares().multiply(stock.getLatestPrice()).setScale(6, RoundingMode.DOWN).toPlainString()
        ));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
