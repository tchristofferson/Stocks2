package com.thedasmc.stocks2.gui.handlers.impl;

import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.gui.PageViewer;
import com.thedasmc.stocks2.gui.handlers.AbstractPortfolioHandler;
import com.thedasmc.stocks2.requests.request.FundsByCreatorRequest;
import com.thedasmc.stocks2.requests.response.FundsByCreatorResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Objects;

public class FundsCreatedByHandler extends AbstractPortfolioHandler {

    public FundsCreatedByHandler(Stocks2 plugin) {
        super(plugin);
    }

    @Override
    protected void handlePreviousPageButtonClick(PageViewer pageViewer) {
        if (pageViewer.getPage() == 0)
            return;

        Player player = Objects.requireNonNull(Bukkit.getPlayer(pageViewer.getViewer()));
        Texts texts = plugin.getTexts();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            FundsByCreatorRequest request = new FundsByCreatorRequest(pageViewer.getOwner(), pageViewer.getPage() - 1);
            FundsByCreatorResponse response;

            try {
                response = plugin.getFundDataInteractor().getFundsByCreator(request);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.FUNDS_CREATED_BY_ERROR, e.getMessage()));
                closeInventory(player);
                return;
            }

            openFetchedInventory(player, pageViewer, response);
        });
    }

    @Override
    protected void handleNextPageButtonClick(PageViewer pageViewer) {
        if (pageViewer.getPage() == pageViewer.getPages() - 1)
            return;

        Player player = Objects.requireNonNull(Bukkit.getPlayer(pageViewer.getViewer()));
        Texts texts = plugin.getTexts();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            FundsByCreatorRequest request = new FundsByCreatorRequest(pageViewer.getOwner(), pageViewer.getPage() + 1);
            FundsByCreatorResponse response;

            try {
                response = plugin.getFundDataInteractor().getFundsByCreator(request);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.FUNDS_CREATED_BY_ERROR, e.getMessage()));
                closeInventory(player);
                return;
            }

            openFetchedInventory(player, pageViewer, response);
        });
    }

    @Override
    protected void handlePortfolioItemClick(ItemStack itemStack) {

    }
}
