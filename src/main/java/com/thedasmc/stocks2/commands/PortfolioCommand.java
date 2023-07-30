package com.thedasmc.stocks2.commands;

import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.gui.GuiFactory;
import com.thedasmc.stocks2.gui.PortfolioViewer;
import com.thedasmc.stocks2.requests.interactors.AbstractPlayerDataInteractor;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;

@CommandAlias("stocks")
public class PortfolioCommand extends AbstractStocksCommand {

    private static final String PORTFOLIO_PERMISSION = "stocks.portfolio";
    private static final String PORTFOLIO_OTHERS_PERMISSION = "stocks.portfolio.others";

    public PortfolioCommand(Stocks2 plugin) {
        super(plugin);
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
    @Syntax("[player]")
    public void onOtherPortfolio(Player player, OnlinePlayer other) {
        openPortfolio(player, other.getPlayer());
    }

    private void openPortfolio(Player viewer, Player owner) {
        AbstractPlayerDataInteractor playerDataInteractor = plugin.getPlayerDataInteractor();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            PortfolioResponse portfolioResponse;

            try {
                portfolioResponse = playerDataInteractor.getPortfolio(owner.getUniqueId(), 0);
            } catch (IOException e) {
                viewer.sendMessage(plugin.getTexts().getErrorText(Texts.Types.FETCH_PORTFOLIO_ERROR, e.getMessage()));
                return;
            }

            final Inventory portfolio = GuiFactory.createPortfolioPage(portfolioResponse, plugin.getTexts());

            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.getPortfolioTracker().track(viewer.getUniqueId(), new PortfolioViewer(viewer.getUniqueId(), owner.getUniqueId(), portfolio, 0, portfolioResponse.getPages()));
                viewer.openInventory(portfolio);
            });
        });
    }

}
