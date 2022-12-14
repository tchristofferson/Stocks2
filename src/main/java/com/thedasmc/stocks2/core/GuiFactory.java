package com.thedasmc.stocks2.core;

import com.thedasmc.stocks2.requests.response.PortfolioResponse;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

//TODO: Use Texts for strings
public class GuiFactory {

    public static final Material STOCK_ITEM_MATERIAL = Material.EMERALD;

    public static Inventory createPortfolioPage(PortfolioResponse portfolioResponse) {
        List<StockResponse> stocks = portfolioResponse.getStocks();
        //Only 45 per page, bottom row reserved for navigation
        stocks = stocks.subList(0, Math.min(45, stocks.size()));
        Inventory portfolio = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Stock Portfolio");
        stocks.forEach(stock -> portfolio.addItem(getStockItem(stock)));

        if (portfolioResponse.getPage() > 0)
            portfolio.setItem(45, getPreviousButton());

        if (portfolioResponse.getPage() < portfolioResponse.getPages() - 1)
            portfolio.setItem(53, getNextButton());

        portfolio.setItem(49, getCloseButton());
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
        sharesItemMeta.setDisplayName(ChatColor.GREEN + startingShares.toPlainString());
        sharesItem.setItemMeta(sharesItemMeta);
        inventory.setItem(4, sharesItem);

        //Adding confirm button to sell inventory
        ItemStack confirmButton = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta confirmMeta = confirmButton.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.GREEN + "Confirm Sale");
        confirmButton.setItemMeta(confirmMeta);
        inventory.setItem(18, confirmButton);

        //Adding cancel button to sell inventory
        ItemStack cancelButton = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta cancelMeta = cancelButton.getItemMeta();
        cancelMeta.setDisplayName(ChatColor.RED + "Cancel Sale");
        cancelButton.setItemMeta(cancelMeta);
        inventory.setItem(26, cancelButton);

        return inventory;
    }

    //Used for the sell menu. Gets a green glass pane
    private static ItemStack getPlusButton(int amount) {
        ItemStack itemStack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Add " + amount + " shares");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    //Used for the sell menu. Gets a red glass pane
    private static ItemStack getSubButton(int amount) {
        ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Subtract " + amount + " shares");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private static ItemStack getStockItem(StockResponse stock) {
        ItemStack itemStack = new ItemStack(STOCK_ITEM_MATERIAL, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + stock.getCompanyName());

        itemMeta.setLore(Arrays.asList(
            ChatColor.GREEN + "Shares: " + stock.getShares().toPlainString(),
            ChatColor.GREEN + "Invested: " + new BigDecimal(stock.getCentsInvested()).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN).toPlainString(),
            ChatColor.GREEN + "Value: " + stock.getShares().multiply(stock.getLatestPrice()).setScale(6, RoundingMode.DOWN).toPlainString()
        ));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static ItemStack getCloseButton() {
        ItemStack itemStack = new ItemStack(Material.BARRIER, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Close Portfolio");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private static ItemStack getNextButton() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        //MHF_ArrowRight
        itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("50c8510b-5ea0-4d60-be9a-7d542d6cd156")));
        itemMeta.setDisplayName(ChatColor.GRAY + "Next Page -->");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private static ItemStack getPreviousButton() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        //MHF_ArrowLeft
        itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString("a68f0b64-8d14-4000-a95f-4b9ba14f8df9")));
        itemMeta.setDisplayName(ChatColor.GRAY + "<-- Previous Page");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
