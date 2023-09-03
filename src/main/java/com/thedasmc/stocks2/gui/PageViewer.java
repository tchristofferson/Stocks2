package com.thedasmc.stocks2.gui;

import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class PageViewer {

    private UUID viewer;
    private UUID owner;
    private Inventory openPage;
    private InventoryType inventoryType;
    private int page;
    private int pages;

    public PageViewer(UUID viewer, UUID owner, Inventory openPage, InventoryType inventoryType, int page, int pages) {
        this.viewer = viewer;
        this.owner = owner;
        this.openPage = openPage;
        this.inventoryType = inventoryType;
        this.page = page;
        this.pages = pages;
    }

    public UUID getViewer() {
        return viewer;
    }

    public void setViewer(UUID viewer) {
        this.viewer = viewer;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public Inventory getOpenPage() {
        return openPage;
    }

    public void setOpenPage(Inventory openPage) {
        this.openPage = openPage;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public enum InventoryType {
        STOCK_PORTFOLIO,
        FUND_PORTFOLIO,
        FUNDS_CREATED_BY
    }
}
