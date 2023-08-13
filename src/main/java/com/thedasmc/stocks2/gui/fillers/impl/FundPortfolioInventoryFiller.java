package com.thedasmc.stocks2.gui.fillers.impl;

import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.gui.fillers.AbstractInventoryFiller;
import com.thedasmc.stocks2.requests.response.FundResponse;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.thedasmc.stocks2.common.Constants.STOCK_ITEM_MATERIAL;

public class FundPortfolioInventoryFiller extends AbstractInventoryFiller<FundResponse> {

    public FundPortfolioInventoryFiller(Inventory inventory, Collection<FundResponse> source, Texts texts) {
        super(inventory, source, texts);
    }

    @Override
    protected ItemStack getSlotItem(FundResponse fund) {
        ItemStack itemStack = new ItemStack(STOCK_ITEM_MATERIAL, 1);
        ItemMeta itemMeta = Objects.requireNonNull(itemStack.getItemMeta());

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(Stocks2.getFundIdKey(), PersistentDataType.LONG, fund.getFundId());

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', fund.getName()));
        List<String> lore = itemMeta.getLore() == null ? new LinkedList<>() : itemMeta.getLore();
        lore.add(ChatColor.GREEN + "Fund ID: " + ChatColor.GRAY + fund.getFundId());

        Instant created = Instant.ofEpochMilli(fund.getCreated());
        LocalDate createdDate = created.atZone(ZoneId.of("America/New_York")).toLocalDate();
        lore.add(ChatColor.GREEN + "Created On: " + createdDate);

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
