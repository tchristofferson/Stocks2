package com.thedasmc.stocks2.gui;

import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.gui.fillers.impl.FundPortfolioInventoryFiller;
import com.thedasmc.stocks2.gui.fillers.impl.StockPortfolioInventoryFiller;
import com.thedasmc.stocks2.requests.response.FundResponse;
import com.thedasmc.stocks2.requests.response.StockDataResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.thedasmc.stocks2.common.Constants.STOCK_GUI_INV_SLOTS;
import static com.thedasmc.stocks2.common.Constants.STOCK_GUI_MAX;

//TODO: Use Texts for strings
public class GuiFactory {

    public static Inventory createFundPage(String title, List<FundResponse> funds, int page, int totalPages, Texts texts) {
        funds = funds.subList(0, Math.min(STOCK_GUI_MAX, funds.size()));
        Inventory inventory = Bukkit.createInventory(null, STOCK_GUI_INV_SLOTS, title);
        new FundPortfolioInventoryFiller(inventory, funds, texts).fill(page, totalPages);

        return inventory;
    }

    public static Inventory createStockPage(String title, List<StockDataResponse> stocks, int page, int totalPages, Texts texts) {
        stocks = stocks.subList(0, Math.min(STOCK_GUI_MAX, stocks.size()));
        Inventory inventory = Bukkit.createInventory(null, STOCK_GUI_INV_SLOTS, title);
        new StockPortfolioInventoryFiller(inventory, stocks, texts).fill(page, totalPages);

        return inventory;
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

}
