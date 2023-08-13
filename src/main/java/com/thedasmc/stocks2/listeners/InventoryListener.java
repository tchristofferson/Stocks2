package com.thedasmc.stocks2.listeners;

import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Constants;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.gui.GuiFactory;
import com.thedasmc.stocks2.gui.GuiTracker;
import com.thedasmc.stocks2.gui.PortfolioViewer;
import com.thedasmc.stocks2.requests.response.AbstractPageResponse;
import com.thedasmc.stocks2.requests.response.FundPortfolioResponse;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static com.thedasmc.stocks2.common.Constants.*;

//TODO: TODO: Clean up code to better handle portfolio vs fund portfolio logic
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

        Texts texts = plugin.getTexts();
        int slot = event.getSlot();

        if (slot == PORTFOLIO_PREVIOUS_BUTTON) {//previous button
            if (portfolioViewer.getPage() == 0)
                return;

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (portfolioViewer.getInventoryType() == PortfolioViewer.InventoryType.PORTFOLIO) {
                    PortfolioResponse portfolioResponse;

                    try {
                        portfolioResponse = plugin.getPlayerDataInteractor().getPortfolio(clicker.getUniqueId(), portfolioViewer.getPage() - 1);
                    } catch (IOException e) {
                        clicker.sendMessage(texts.getErrorText(Texts.Types.FETCH_PORTFOLIO_ERROR, e.getMessage()));
                        closeInventory(clicker);
                        return;
                    }

                    openFetchedPage(clicker, portfolioViewer, portfolioResponse);
                } else if (portfolioViewer.getInventoryType() == PortfolioViewer.InventoryType.FUND_PORTFOLIO) {
                    FundPortfolioResponse fundPortfolioResponse;

                    try {
                        fundPortfolioResponse = plugin.getFundDataInteractor().getFundPortfolio(clicker.getUniqueId(), portfolioViewer.getPage() - 1);
                    } catch (IOException e) {
                        clicker.sendMessage(texts.getErrorText(Texts.Types.FUND_PORTFOLIO_ERROR, e.getMessage()));
                        closeInventory(clicker);
                        return;
                    }

                    openFetchedPage(clicker, portfolioViewer, fundPortfolioResponse);
                }
            });
        } else if (slot == PORTFOLIO_NEXT_BUTTON) {//next button
            if (portfolioViewer.getPage() == portfolioViewer.getPages() - 1)
                return;

            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                if (portfolioViewer.getInventoryType() == PortfolioViewer.InventoryType.PORTFOLIO) {
                    PortfolioResponse portfolioResponse;

                    try {
                        portfolioResponse = plugin.getPlayerDataInteractor().getPortfolio(clicker.getUniqueId(), portfolioViewer.getPage() + 1);
                    } catch (IOException e) {
                        clicker.sendMessage(texts.getErrorText(Texts.Types.FETCH_PORTFOLIO_ERROR, e.getMessage()));
                        closeInventory(clicker);
                        return;
                    }

                    openFetchedPage(clicker, portfolioViewer, portfolioResponse);
                } else if (portfolioViewer.getInventoryType() == PortfolioViewer.InventoryType.FUND_PORTFOLIO) {
                    FundPortfolioResponse fundPortfolioResponse;

                    try {
                        fundPortfolioResponse = plugin.getFundDataInteractor().getFundPortfolio(clicker.getUniqueId(), portfolioViewer.getPage() + 1);
                    } catch (IOException e) {
                        clicker.sendMessage(texts.getErrorText(Texts.Types.FUND_PORTFOLIO_ERROR, e.getMessage()));
                        closeInventory(clicker);
                        return;
                    }

                    openFetchedPage(clicker, portfolioViewer, fundPortfolioResponse);
                }
            });
        } else if (slot == STOCK_GUI_CLOSE_BUTTON) {//close button
            Bukkit.getScheduler().runTask(plugin, clicker::closeInventory);
        } else if (event.getCursor().getType() == Constants.STOCK_ITEM_MATERIAL) {
            //TODO: Handle stock item click
        }
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

    private void closeInventory(Player clicker) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (clicker.isOnline())
                clicker.closeInventory();
        });
    }

    //TODO: Use texts for inventory title
    private void openFetchedPage(Player clicker, PortfolioViewer portfolioViewer, AbstractPageResponse pageResponse) {
        Inventory inventory;

        if (pageResponse instanceof PortfolioResponse) {
            PortfolioResponse portfolioResponse = (PortfolioResponse) pageResponse;

            inventory = GuiFactory.createStockPage(ChatColor.GOLD + "Stock Portfolio",
                new ArrayList<>(portfolioResponse.getStocks()), portfolioResponse.getPage(), portfolioResponse.getPages(), plugin.getTexts());
        } else if (pageResponse instanceof FundPortfolioResponse) {
            FundPortfolioResponse fundPortfolioResponse = (FundPortfolioResponse) pageResponse;

            inventory = GuiFactory.createFundPage(ChatColor.GOLD + "Fund Portfolio",
                fundPortfolioResponse.getFunds(), fundPortfolioResponse.getPage(), fundPortfolioResponse.getPages(), plugin.getTexts());
        } else {
            throw new UnsupportedOperationException("Unhandled page response logic for class " + pageResponse.getClass().getSimpleName());
        }

        updatePortfolioViewer(clicker, portfolioViewer, inventory, pageResponse.getPage(), pageResponse.getPages());
    }

    private void updatePortfolioViewer(Player clicker, PortfolioViewer portfolioViewer, Inventory inventory, Integer page, Integer pages) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (clicker.isOnline()) {
                clicker.closeInventory();
                portfolioViewer.setPage(page);
                portfolioViewer.setPages(pages);
                portfolioViewer.setOpenPortfolio(inventory);
                plugin.getPortfolioTracker().track(portfolioViewer.getViewer(), portfolioViewer);

                clicker.openInventory(inventory);
            }
        });
    }
}
