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
import org.bukkit.inventory.meta.SkullMeta;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

            final Inventory portfolio = createPortfolio(stocks);

            Bukkit.getScheduler().runTask(plugin, () -> {
                //TODO: Modify web app to include how many pages there are. Then use it here
                plugin.getPortfolioTracker().track(new PortfolioViewer(player.getUniqueId(), player.getUniqueId(), portfolio, 0, 1));
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

        portfolio.setItem(45, getPreviousButton());
        portfolio.setItem(49, getCloseButton());
        portfolio.setItem(53, getNextButton());

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

    private ItemStack getCloseButton() {
        ItemStack itemStack = new ItemStack(Material.BARRIER, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Close Portfolio");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack getNextButton() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        //MHF_ArrowRight
        itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("50c8510b-5ea0-4d60-be9a-7d542d6cd156")));
        itemMeta.setDisplayName(ChatColor.GRAY + "Next Page -->");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack getPreviousButton() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        //MHF_ArrowLeft
        itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("a68f0b64-8d14-4000-a95f-4b9ba14f8df9")));
        itemMeta.setDisplayName(ChatColor.GRAY + "<-- Previous Page");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
