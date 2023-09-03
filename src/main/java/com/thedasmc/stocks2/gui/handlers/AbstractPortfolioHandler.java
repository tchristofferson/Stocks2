package com.thedasmc.stocks2.gui.handlers;

import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.gui.GuiFactory;
import com.thedasmc.stocks2.gui.GuiTracker;
import com.thedasmc.stocks2.gui.PageViewer;
import com.thedasmc.stocks2.gui.handlers.impl.FundPortfolioHandler;
import com.thedasmc.stocks2.gui.handlers.impl.FundsCreatedByHandler;
import com.thedasmc.stocks2.gui.handlers.impl.StockPortfolioHandler;
import com.thedasmc.stocks2.requests.response.AbstractPageResponse;
import com.thedasmc.stocks2.requests.response.FundPortfolioResponse;
import com.thedasmc.stocks2.requests.response.FundsByCreatorResponse;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

import static com.thedasmc.stocks2.common.Constants.*;

public abstract class AbstractPortfolioHandler {

    protected final Stocks2 plugin;

    protected AbstractPortfolioHandler(Stocks2 plugin) {
        this.plugin = plugin;
    }

    public void handle(InventoryClickEvent event) {
        Player clicker = (Player) event.getWhoClicked();
        GuiTracker<UUID, PageViewer> tracker = plugin.getPageTracker();
        PageViewer portfolioViewer = tracker.get(clicker.getUniqueId());

        int slot = event.getSlot();

        if (slot == PORTFOLIO_PREVIOUS_BUTTON) {
            handlePreviousPageButtonClick(portfolioViewer);
        } else if (slot == PORTFOLIO_NEXT_BUTTON) {
            handleNextPageButtonClick(portfolioViewer);
        } else if (slot == STOCK_GUI_CLOSE_BUTTON) {
            closeInventory(clicker);
        } else if (event.getCurrentItem() != null && event.getCurrentItem().getType() == PORTFOLIO_ITEM_MATERIAL) {
            handlePortfolioItemClick(event.getCurrentItem());
        }
    }

    protected abstract void handlePreviousPageButtonClick(PageViewer pageViewer);

    protected abstract void handleNextPageButtonClick(PageViewer pageViewer);

    protected abstract void handlePortfolioItemClick(ItemStack itemStack);

    protected void closeInventory(Player player) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (player.isOnline())
                player.closeInventory();
        });
    }

    /**
     * Opens the fetched portfolio and updates the {@link PageViewer}<br>
     * Run Asynchronously
     * @param player The player opening the inventory
     * @param pageViewer The page viewer representing the player opening the inventory
     * @param pageResponse The {@link AbstractPageResponse} with the portfolio page details
     */
    protected void openFetchedInventory(Player player, PageViewer pageViewer, AbstractPageResponse pageResponse) {
        Inventory inventory;

        if (pageResponse instanceof PortfolioResponse) {
            PortfolioResponse portfolioResponse = (PortfolioResponse) pageResponse;

            inventory = GuiFactory.createStockPage(ChatColor.GOLD + "Stock Portfolio",
                new ArrayList<>(portfolioResponse.getStocks()), portfolioResponse.getPage(), portfolioResponse.getPages(), plugin.getTexts());
        } else if (pageResponse instanceof FundPortfolioResponse) {
            FundPortfolioResponse fundPortfolioResponse = (FundPortfolioResponse) pageResponse;

            inventory = GuiFactory.createFundPage(ChatColor.GOLD + "Fund Portfolio",
                fundPortfolioResponse.getFunds(), fundPortfolioResponse.getPage(), fundPortfolioResponse.getPages(), plugin.getTexts());
        } else if (pageResponse instanceof FundsByCreatorResponse) {
            FundsByCreatorResponse fundsByCreatorResponse = (FundsByCreatorResponse) pageResponse;

            inventory = GuiFactory.createFundPage(ChatColor.GOLD + "Funds",
                fundsByCreatorResponse.getFunds(), fundsByCreatorResponse.getPage(), fundsByCreatorResponse.getPages(), plugin.getTexts());
        } else {
            throw new UnsupportedOperationException("Unhandled page response logic for class " + pageResponse.getClass().getSimpleName());
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            if (player.isOnline()) {
                player.closeInventory();
                pageViewer.setPage(pageResponse.getPage());
                pageViewer.setPages(pageResponse.getPages());
                pageViewer.setOpenPage(inventory);
                plugin.getPageTracker().track(pageViewer.getViewer(), pageViewer);

                player.openInventory(inventory);
            }
        });
    }

    /**
     * Get an instance of {@link AbstractPortfolioHandler} for the specified {@link PageViewer.InventoryType}
     * @param type The inventory type
     * @param plugin The plugin
     * @return An instance of {@link AbstractPortfolioHandler}
     */
    public static AbstractPortfolioHandler getPortfolioHandler(PageViewer.InventoryType type, Stocks2 plugin) {
        switch (type) {
            case STOCK_PORTFOLIO:
                return new StockPortfolioHandler(plugin);
            case FUND_PORTFOLIO:
                return new FundPortfolioHandler(plugin);
            case FUNDS_CREATED_BY:
                return new FundsCreatedByHandler(plugin);
            default:
                throw new UnsupportedOperationException("Unsupported InventoryType!");
        }
    }

}
