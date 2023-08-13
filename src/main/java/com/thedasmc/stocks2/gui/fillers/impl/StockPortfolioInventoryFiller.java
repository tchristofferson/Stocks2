package com.thedasmc.stocks2.gui.fillers.impl;

import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.gui.fillers.AbstractInventoryFiller;
import com.thedasmc.stocks2.requests.response.StockDataResponse;
import com.thedasmc.stocks2.requests.response.StockResponse;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.thedasmc.stocks2.common.Constants.STOCK_ITEM_MATERIAL;

public class StockPortfolioInventoryFiller extends AbstractInventoryFiller<StockDataResponse> {

    public StockPortfolioInventoryFiller(Inventory inventory, Collection<StockDataResponse> source, Texts texts) {
        super(inventory, source, texts);
    }

    @Override
    public ItemStack getSlotItem(StockDataResponse stock) {
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
}
