package com.thedasmc.stocks2.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.core.GuiFactory;
import com.thedasmc.stocks2.core.PortfolioViewer;
import com.thedasmc.stocks2.requests.AbstractPlayerDataInteractor;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import org.bukkit.Bukkit;
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
        openPortfolio(player, player);
    }

    @Subcommand("portfolio")
    @CommandPermission(PORTFOLIO_OTHERS_PERMISSION)
    @Description("View another player's portfolio")
    public void onOtherPortfolio(Player player, OnlinePlayer other) {
        openPortfolio(player, other.getPlayer());
    }

    private void openPortfolio(Player viewer, Player owner) {
        AbstractPlayerDataInteractor playerDataInteractor = plugin.getPlayerDataRequester();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            PortfolioResponse portfolioResponse;

            try {
                portfolioResponse = playerDataInteractor.getPortfolio(owner.getUniqueId(), 0);
            } catch (IOException e) {
                viewer.sendMessage(plugin.getTexts().getErrorText(Texts.Types.FETCH_PORTFOLIO_ERROR, e.getMessage()));
                return;
            }

            final Inventory portfolio = GuiFactory.createPortfolioPage(portfolioResponse);

            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.getPortfolioTracker().track(new PortfolioViewer(viewer.getUniqueId(), owner.getUniqueId(), portfolio, 0, portfolioResponse.getPages()));
                viewer.openInventory(portfolio);
            });
        });
    }

}
