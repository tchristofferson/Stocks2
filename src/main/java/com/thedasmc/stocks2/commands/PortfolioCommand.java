package com.thedasmc.stocks2.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.core.PortfolioFactory;
import com.thedasmc.stocks2.core.PortfolioViewer;
import com.thedasmc.stocks2.requests.AbstractPlayerDataRequester;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;

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
            PortfolioResponse portfolioResponse;

            try {
                portfolioResponse = playerDataRequester.getPortfolio(player.getUniqueId(), 0);
            } catch (IOException e) {
                player.sendMessage(ChatColor.RED + "[Stocks]There was an error fetching your portfolio: " + e.getMessage() + "!");
                return;
            }

            final Inventory portfolio = PortfolioFactory.createPortfolioPage(portfolioResponse);

            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.getPortfolioTracker().track(new PortfolioViewer(player.getUniqueId(), player.getUniqueId(), portfolio, 0, portfolioResponse.getPages()));
                player.openInventory(portfolio);
            });
        });
    }

    @Subcommand("portfolio")
    @CommandPermission(PORTFOLIO_OTHERS_PERMISSION)
    @Description("View another player's portfolio")
    public void onOtherPortfolio(Player player, OnlinePlayer other) {

    }

}
