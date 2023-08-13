package com.thedasmc.stocks2.commands.stocks;

import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.commands.AbstractStocksCommand;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.gui.GuiFactory;
import com.thedasmc.stocks2.gui.PortfolioViewer;
import com.thedasmc.stocks2.requests.interactors.AbstractPlayerDataInteractor;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.ArrayList;

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

            //TODO: Use texts for inventory title
            final Inventory portfolio = GuiFactory.createStockPage(ChatColor.GOLD + "Stock Portfolio",
                new ArrayList<>(portfolioResponse.getStocks()), portfolioResponse.getPage(), portfolioResponse.getPages(), plugin.getTexts());

            Bukkit.getScheduler().runTask(plugin, () -> {
                if (viewer.isOnline()) {
                    PortfolioViewer portfolioViewer = new PortfolioViewer(viewer.getUniqueId(), owner.getUniqueId(),
                        portfolio, PortfolioViewer.InventoryType.PORTFOLIO, 0, portfolioResponse.getPages());
                    plugin.getPortfolioTracker().track(viewer.getUniqueId(), portfolioViewer);
                    viewer.openInventory(portfolio);
                }
            });
        });
    }

}
