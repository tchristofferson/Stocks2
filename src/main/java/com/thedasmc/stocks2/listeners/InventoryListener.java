package com.thedasmc.stocks2.listeners;

import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.gui.GuiTracker;
import com.thedasmc.stocks2.gui.PortfolioViewer;
import com.thedasmc.stocks2.gui.handlers.AbstractPortfolioHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

import static com.thedasmc.stocks2.common.Constants.STOCK_GUI_CLOSE_BUTTON;

public class InventoryListener implements Listener {

    private final Stocks2 plugin;

    public InventoryListener(Stocks2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClickForPortfolio(InventoryClickEvent event) {
        Player clicker = (Player) event.getWhoClicked();
        GuiTracker<UUID, PortfolioViewer> tracker = plugin.getPortfolioTracker();
        PortfolioViewer portfolioViewer = tracker.get(clicker.getUniqueId());

        if (portfolioViewer == null || !portfolioViewer.getOpenPortfolio().equals(event.getClickedInventory()))
            return;

        event.setCancelled(true);

        if (event.getCursor() == null || !event.isLeftClick() || event.getClick() == ClickType.DOUBLE_CLICK)
            return;

        AbstractPortfolioHandler
            .getPortfolioHandler(portfolioViewer.getInventoryType(), plugin)
            .handle(event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClickForPopular(InventoryClickEvent event) {
        Player clicker = (Player) event.getWhoClicked();
        Inventory popularInventory = plugin.getPopularTracker().get(clicker.getUniqueId());

        if (popularInventory == null)
            return;

        event.setCancelled(true);
        int slot = event.getSlot();

        if (slot == STOCK_GUI_CLOSE_BUTTON)
            Bukkit.getScheduler().runTask(plugin, clicker::closeInventory);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClickForSellMenu(InventoryClickEvent event) {
        //TODO: Handle inventory click for sell menu
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        plugin.getPortfolioTracker().untrack(event.getPlayer().getUniqueId());
        plugin.getPopularTracker().untrack(event.getPlayer().getUniqueId());
    }
}
