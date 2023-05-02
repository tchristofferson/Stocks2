package com.thedasmc.stocks2.core;

import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.requests.response.PortfolioResponse;
import com.thedasmc.stocks2.requests.response.StockDataResponse;
import com.thedasmc.stocks2.requests.response.StockResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.thedasmc.stocks2.common.Constants.*;

//TODO: Use Texts for strings
public class GuiFactory {

    public static final Material STOCK_ITEM_MATERIAL = Material.EMERALD;

    public static Inventory createStockPage(String title, List<? extends StockDataResponse> stocks, Texts texts) {
        stocks = stocks.subList(0, Math.min(STOCK_GUI_MAX, stocks.size()));
        Inventory inventory = Bukkit.createInventory(null, STOCK_GUI_INV_SLOTS, title);
        Iterator<? extends StockDataResponse> stockIterator = stocks.iterator();
        //1 empty row above, 1 empty row below, one row for navigation
        int rows = (STOCK_GUI_INV_SLOTS / 9) - 3;

        for (int row = 0; row < rows; row++) {
            //row * 9 gets the first slot of the row.
            //Add 2 to get 2 slots in from the left for padding
            //Add 9 to leave the first row empty
            int startSlot = (row * 9) + 2 + 9;
            int endSlot = startSlot + 4;

            for (int slot = startSlot; slot <= endSlot; slot++) {
                if (stockIterator.hasNext()) {
                    StockDataResponse stock = stockIterator.next();
                    inventory.setItem(slot, getStockItem(stock, texts));
                } else {
                    ItemStack itemStack = new ItemStack(Material.STICK, 1);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    Objects.requireNonNull(itemMeta).setDisplayName("Empty");
                    itemStack.setItemMeta(itemMeta);

                    inventory.setItem(slot, itemStack);
                }
            }
        }

        for (int i = 0; i < inventory.getSize() - 9; i++) {
            if (inventory.getItem(i) == null) {
                ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                ItemMeta itemMeta = itemStack.getItemMeta();
                Objects.requireNonNull(itemMeta).setDisplayName("N/A");
                itemStack.setItemMeta(itemMeta);

                inventory.setItem(i, itemStack);
            }
        }

        inventory.setItem(STOCK_GUI_CLOSE_BUTTON, getCloseButton());
        return inventory;
    }

    public static Inventory createPortfolioPage(PortfolioResponse portfolioResponse, Texts texts) {
        List<StockResponse> stocks = portfolioResponse.getStocks();
        Inventory portfolio = createStockPage(ChatColor.GOLD + "Stock Portfolio", stocks, texts);

        if (portfolioResponse.getPage() > 0)
            portfolio.setItem(PORTFOLIO_PREVIOUS_BUTTON, getPreviousButton());

        if (portfolioResponse.getPage() < portfolioResponse.getPages() - 1)
            portfolio.setItem(PORTFOLIO_NEXT_BUTTON, getNextButton());

        return portfolio;
    }

    public static Inventory createSellInventory(BigDecimal startingShares) {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Sell Shares");
        List<Integer> buttonAmounts = Arrays.asList(1000, 100, 50, 10, 1);

        int addSlot = 11;
        int subSlot = 20;
        //Adding the Add/Subtract x shares buttons to the sell inventory
        for (Integer amount : buttonAmounts) {
            inventory.setItem(addSlot++, getPlusButton(amount));
            inventory.setItem(subSlot++, getSubButton(amount));
        }

        //Adding the emerald item stack that displays the number of shares they want to sell
        ItemStack sharesItem = new ItemStack(Material.EMERALD, 1);
        ItemMeta sharesItemMeta = sharesItem.getItemMeta();
        Objects.requireNonNull(sharesItemMeta).setDisplayName(ChatColor.GREEN + startingShares.toPlainString());
        sharesItem.setItemMeta(sharesItemMeta);
        inventory.setItem(4, sharesItem);

        //Adding confirm button to sell inventory
        ItemStack confirmButton = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta confirmMeta = confirmButton.getItemMeta();
        Objects.requireNonNull(confirmMeta).setDisplayName(ChatColor.GREEN + "Confirm Sale");
        confirmButton.setItemMeta(confirmMeta);
        inventory.setItem(18, confirmButton);

        //Adding cancel button to sell inventory
        ItemStack cancelButton = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta cancelMeta = cancelButton.getItemMeta();
        Objects.requireNonNull(cancelMeta).setDisplayName(ChatColor.RED + "Cancel Sale");
        cancelButton.setItemMeta(cancelMeta);
        inventory.setItem(26, cancelButton);

        return inventory;
    }

    //Used for the sell menu. Gets a green glass pane
    private static ItemStack getPlusButton(int amount) {
        ItemStack itemStack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.GREEN + "Add " + amount + " shares");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    //Used for the sell menu. Gets a red glass pane
    private static ItemStack getSubButton(int amount) {
        ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.RED + "Subtract " + amount + " shares");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private static ItemStack getStockItem(StockDataResponse stock, Texts texts) {
        ItemStack itemStack = new ItemStack(STOCK_ITEM_MATERIAL, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + stock.getSymbol());

        List<String> lore = itemMeta.getLore() == null ? new LinkedList<>() : itemMeta.getLore();
        lore.add(ChatColor.GREEN + "Company Name: " + ChatColor.GRAY + stock.getCompanyName());
        lore.add(ChatColor.GREEN + "Price: " + ChatColor.GRAY + texts.getMoneySymbol() + stock.getLatestPrice());

        /* START change % */
        BigDecimal change = stock.getChangePercent();

        if (change != null) {
            ChatColor changeStringColor = ChatColor.GRAY;

            if (change.compareTo(BigDecimal.ZERO) < 0) {
                changeStringColor = ChatColor.RED;
            } else if (change.compareTo(BigDecimal.ZERO) > 0) {
                changeStringColor = ChatColor.GREEN;
            }

            String changeString = change.multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.DOWN)
                .toPlainString();

            lore.add(ChatColor.GREEN + "Change: " + changeStringColor + changeString);
        }
        /* END change % */

        //Add shares, invested, and value to lore
        if (stock instanceof StockResponse) {
            StockResponse stockResponse = (StockResponse) stock;
            BigDecimal invested = new BigDecimal(stockResponse.getCentsInvested()).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN);
            BigDecimal value = stockResponse.getShares().multiply(stock.getLatestPrice()).setScale(6, RoundingMode.DOWN);

            lore.add(ChatColor.GREEN + "Shares: " + ChatColor.GRAY + stockResponse.getShares().toPlainString());
            lore.add(ChatColor.GREEN + "Invested: " + ChatColor.GRAY + invested.toPlainString());

            String valueLore = ChatColor.GREEN + "Value: " + ChatColor.GRAY + value.toPlainString();

            if (value.compareTo(invested) < 0) {
                valueLore += ChatColor.RED + "⌄";
            } else if (value.compareTo(invested) > 0) {
                valueLore += ChatColor.GREEN + "⌃";
            }

            lore.add(valueLore);
        }

        BigDecimal openPrice = stock.getOpen();

        if (openPrice != null) {
            lore.add(ChatColor.GREEN + "Open: " + ChatColor.GRAY + texts.getMoneySymbol() + openPrice.toPlainString());
        }

        BigDecimal closePrice = stock.getClose();

        if (closePrice != null) {
            lore.add(ChatColor.GREEN + "Close: " + ChatColor.GRAY + texts.getMoneySymbol() + closePrice.toPlainString());
        }

        if (stock.getWeek52High() != null) {
            lore.add(ChatColor.GREEN + "52 Week High: " + ChatColor.GRAY + texts.getMoneySymbol() + stock.getWeek52High());
        }

        if (stock.getWeek52Low() != null) {
            lore.add(ChatColor.GREEN + "52 Week Low: " + ChatColor.GRAY + texts.getMoneySymbol() + stock.getWeek52Low());
        }

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private static ItemStack getCloseButton() {
        ItemStack itemStack = new ItemStack(Material.BARRIER, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.RED + "Close Portfolio");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private static ItemStack getNextButton() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setOwner("MHF_ArrowRight");
        itemMeta.setDisplayName(ChatColor.GRAY + "Next Page -->");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private static ItemStack getPreviousButton() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setOwner("MHF_ArrowLeft");
        itemMeta.setDisplayName(ChatColor.GRAY + "<-- Previous Page");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
