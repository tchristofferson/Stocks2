package com.thedasmc.stocks2.gui.handlers.impl;

import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.gui.PageViewer;
import com.thedasmc.stocks2.gui.handlers.AbstractPortfolioHandler;
import com.thedasmc.stocks2.requests.response.FundPortfolioResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Objects;

public class FundPortfolioHandler extends AbstractPortfolioHandler {

    public FundPortfolioHandler(Stocks2 plugin) {
        super(plugin);
    }

    @Override
    protected void handlePreviousPageButtonClick(PageViewer pageViewer) {
        if (pageViewer.getPage() == 0)
            return;

        Player player = Objects.requireNonNull(Bukkit.getPlayer(pageViewer.getViewer()));
        Texts texts = plugin.getTexts();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            FundPortfolioResponse fundPortfolioResponse;

            try {
                fundPortfolioResponse = plugin.getFundDataInteractor().getFundPortfolio(pageViewer.getOwner(), pageViewer.getPage() - 1);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.FUND_PORTFOLIO_ERROR, e.getMessage()));
                closeInventory(player);
                return;
            }

            openFetchedInventory(player, pageViewer, fundPortfolioResponse);
        });
    }

    @Override
    protected void handleNextPageButtonClick(PageViewer pageViewer) {
        if (pageViewer.getPage() == pageViewer.getPages() - 1)
            return;

        Player player = Objects.requireNonNull(Bukkit.getPlayer(pageViewer.getViewer()));
        Texts texts = plugin.getTexts();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            FundPortfolioResponse fundPortfolioResponse;

            try {
                fundPortfolioResponse = plugin.getFundDataInteractor().getFundPortfolio(pageViewer.getOwner(), pageViewer.getPage() + 1);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.FUND_PORTFOLIO_ERROR, e.getMessage()));
                closeInventory(player);
                return;
            }

            openFetchedInventory(player, pageViewer, fundPortfolioResponse);
        });
    }

    @Override
    protected void handlePortfolioItemClick(ItemStack itemStack) {
        //TODO: Implement handlePortfolioItemClick
    }
}
