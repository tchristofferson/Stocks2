package com.thedasmc.stocks2.gui.handlers.impl;

import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.gui.PortfolioViewer;
import com.thedasmc.stocks2.gui.handlers.AbstractPortfolioHandler;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Objects;

public class StockPortfolioHandler extends AbstractPortfolioHandler {

    public StockPortfolioHandler(Stocks2 plugin) {
        super(plugin);
    }

    @Override
    protected void handlePreviousPageButtonClick(PortfolioViewer portfolioViewer) {
        if (portfolioViewer.getPage() == 0)
            return;

        Player player = Objects.requireNonNull(Bukkit.getPlayer(portfolioViewer.getViewer()));
        Texts texts = plugin.getTexts();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            PortfolioResponse portfolioResponse;

            try {
                portfolioResponse = plugin.getPlayerDataInteractor().getPortfolio(portfolioViewer.getOwner(), portfolioViewer.getPage() - 1);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.FETCH_PORTFOLIO_ERROR, e.getMessage()));
                closeInventory(player);
                return;
            }

            openFetchedInventory(player, portfolioViewer, portfolioResponse);
        });
    }

    @Override
    protected void handleNextPageButtonClick(PortfolioViewer portfolioViewer) {
        if (portfolioViewer.getPage() == portfolioViewer.getPages() - 1)
            return;

        Player player = Objects.requireNonNull(Bukkit.getPlayer(portfolioViewer.getViewer()));
        Texts texts = plugin.getTexts();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            PortfolioResponse portfolioResponse;

            try {
                portfolioResponse = plugin.getPlayerDataInteractor().getPortfolio(portfolioViewer.getOwner(), portfolioViewer.getPage() + 1);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.FETCH_PORTFOLIO_ERROR, e.getMessage()));
                closeInventory(player);
                return;
            }

            openFetchedInventory(player, portfolioViewer, portfolioResponse);
        });
    }

    @Override
    protected void handlePortfolioItemClick(ItemStack itemStack) {
        //TODO: Implement handlePortfolioItemClick
    }
}
