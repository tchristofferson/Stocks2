package com.thedasmc.stocks2.gui.fillers;

import com.thedasmc.stocks2.common.Texts;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public abstract class AbstractPortfolioInventoryFiller<T> {

    private static final int MIN_INV_SIZE = 36;

    private final Inventory inventory;
    private final Collection<T> source;
    protected final Texts texts;

    protected AbstractPortfolioInventoryFiller(Inventory inventory, Collection<T> source, Texts texts) {
        this.inventory = inventory;
        this.source = source;
        this.texts = texts;
    }

    public final void fill(int page, int totalPages) {
        if (this.inventory.getSize() < MIN_INV_SIZE)
            throw new IllegalArgumentException("inventory size must be >= " + MIN_INV_SIZE);

        Iterator<T> sourceIterator = this.source.iterator();

        //1 empty row above, 1 empty row below, one row for navigation
        int rows = (this.inventory.getSize() / 9) - 3;

        for (int row = 0; row < rows; row++) {
            //row * 9 gets the first slot of the row.
            //Add 2 to get 2 slots in from the left for padding
            //Add 9 to leave the first row empty
            int startSlot = (row * 9) + 2 + 9;
            int endSlot = startSlot + 4;

            for (int slot = startSlot; slot <= endSlot; slot++) {
                if (sourceIterator.hasNext()) {
                    T dataSource = sourceIterator.next();
                    inventory.setItem(slot, getSlotItem(dataSource));
                } else {
                    inventory.setItem(slot, getNullSourceItem());
                }
            }
        }

        int previousButtonSlot = inventory.getSize() - 9;
        int nextButtonSlot = inventory.getSize() - 1;
        int closeButtonSlot = inventory.getSize() - 5;

        inventory.setItem(closeButtonSlot, getCloseButton());

        if (page > 0)
            inventory.setItem(previousButtonSlot, getPreviousButton());

        if (page < totalPages - 1)
            inventory.setItem(nextButtonSlot, getNextButton());

        for (int i = 0; i < inventory.getSize() - 9; i++) {
            if (inventory.getItem(i) == null)
                inventory.setItem(i, getEmptySlot());
        }
    }

    protected abstract ItemStack getSlotItem(T dataSource);

    protected ItemStack getNullSourceItem() {
        ItemStack itemStack = new ItemStack(Material.STICK, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName("Empty");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    protected ItemStack getCloseButton() {
        ItemStack itemStack = new ItemStack(Material.BARRIER, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.RED + "Close Portfolio");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    protected ItemStack getNextButton() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setOwner("MHF_ArrowRight");
        itemMeta.setDisplayName(ChatColor.GRAY + "Next Page -->");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    protected ItemStack getPreviousButton() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setOwner("MHF_ArrowLeft");
        itemMeta.setDisplayName(ChatColor.GRAY + "<-- Previous Page");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    protected ItemStack getEmptySlot() {
        ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName("N/A");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
