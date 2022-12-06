package com.thedasmc.stocks2.listeners;

import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.core.PortfolioTracker;
import com.thedasmc.stocks2.core.PortfolioViewer;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    private final Stocks2 plugin;

    public InventoryListener(Stocks2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClick() != ClickType.LEFT)
            return;

        HumanEntity clicker = event.getWhoClicked();
        PortfolioTracker tracker = plugin.getPortfolioTracker();
        PortfolioViewer portfolioViewer = tracker.getViewer(clicker.getUniqueId());

        if (portfolioViewer == null || !portfolioViewer.getOpenPortfolio().equals(event.getClickedInventory()))
            return;

        event.setCancelled(true);

        if (event.getCursor() == null)
            return;

        int slot = event.getSlot();

        if (slot == 45) {//previous button
            if (portfolioViewer.getPage() == 0)
                return;

            //TODO: Open previous page
        } else if (slot == 53) {//next button
            if (portfolioViewer.getPage() == portfolioViewer.getPages() - 1)
                return;

            //TODO: Open next page
        } else if (slot == 49) {//close button
            Bukkit.getScheduler().runTask(plugin, clicker::closeInventory);
        } else {
            //TODO: Handle stock item click
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity player = event.getPlayer();
        PortfolioTracker portfolioTracker = plugin.getPortfolioTracker();
        PortfolioViewer portfolioViewer = portfolioTracker.getViewer(player.getUniqueId());

        if (portfolioViewer == null)
            return;

        portfolioTracker.untrack(player.getUniqueId());
    }
}
