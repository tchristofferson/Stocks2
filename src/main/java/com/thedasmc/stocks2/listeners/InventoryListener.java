package com.thedasmc.stocks2.listeners;

import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.core.GuiFactory;
import com.thedasmc.stocks2.core.PortfolioTracker;
import com.thedasmc.stocks2.core.PortfolioViewer;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.io.IOException;

public class InventoryListener implements Listener {

    private final Stocks2 plugin;

    public InventoryListener(Stocks2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClickForPortfolio(InventoryClickEvent event) {
        Player clicker = (Player) event.getWhoClicked();
        PortfolioTracker tracker = plugin.getPortfolioTracker();
        PortfolioViewer portfolioViewer = tracker.getViewer(clicker.getUniqueId());

        if (portfolioViewer == null || !portfolioViewer.getOpenPortfolio().equals(event.getClickedInventory()))
            return;

        event.setCancelled(true);

        if (event.getCursor() == null || event.getClick() != ClickType.LEFT)
            return;

        Texts texts = plugin.getTexts();
        int slot = event.getSlot();

        if (slot == 45) {//previous button
            if (portfolioViewer.getPage() == 0)
                return;

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                PortfolioResponse portfolioResponse;

                try {
                    portfolioResponse = plugin.getPlayerDataRequester().getPortfolio(clicker.getUniqueId(), portfolioViewer.getPage() - 1);
                } catch (IOException e) {
                    clicker.sendMessage(texts.getErrorText(Texts.Types.ERROR_FETCHING_PORTFOLIO, e.getMessage()));
                    closeInventory(clicker);
                    return;
                }

                openFetchedPortfolioPage(clicker, portfolioViewer, portfolioResponse);
            });
        } else if (slot == 53) {//next button
            if (portfolioViewer.getPage() == portfolioViewer.getPages() - 1)
                return;

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                PortfolioResponse portfolioResponse;

                try {
                    portfolioResponse = plugin.getPlayerDataRequester().getPortfolio(clicker.getUniqueId(), portfolioViewer.getPage() + 1);
                } catch (IOException e) {
                    clicker.sendMessage(texts.getErrorText(Texts.Types.ERROR_FETCHING_PORTFOLIO, e.getMessage()));
                    closeInventory(clicker);
                    return;
                }

                openFetchedPortfolioPage(clicker, portfolioViewer, portfolioResponse);
            });
        } else if (slot == 49) {//close button
            Bukkit.getScheduler().runTask(plugin, clicker::closeInventory);
        } else if (event.getCursor().getType() == GuiFactory.STOCK_ITEM_MATERIAL) {
            //TODO: Handle stock item click
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClickForSellMenu(InventoryClickEvent event) {
        //TODO: Handle inventory click for sell menu
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        plugin.getPortfolioTracker().untrack(event.getPlayer().getUniqueId());
    }

    private void closeInventory(Player clicker) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (clicker.isOnline())
                clicker.closeInventory();
        });
    }

    private void openFetchedPortfolioPage(Player clicker, PortfolioViewer portfolioViewer, PortfolioResponse portfolioResponse) {
        Inventory inventory = GuiFactory.createPortfolioPage(portfolioResponse);
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (clicker.isOnline()) {
                clicker.closeInventory();
                portfolioViewer.setPage(portfolioResponse.getPage());
                portfolioViewer.setPages(portfolioResponse.getPages());
                portfolioViewer.setOpenPortfolio(inventory);
                plugin.getPortfolioTracker().track(portfolioViewer);

                clicker.openInventory(inventory);
            }
        });
    }
}
