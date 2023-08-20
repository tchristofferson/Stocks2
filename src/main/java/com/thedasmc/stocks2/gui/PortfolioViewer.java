package com.thedasmc.stocks2.gui;

import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class PortfolioViewer {

    private UUID viewer;
    private UUID owner;
    private Inventory openPortfolio;
    private InventoryType inventoryType;
    private int page;
    private int pages;

    public PortfolioViewer(UUID viewer, UUID owner, Inventory openPortfolio, InventoryType inventoryType, int page, int pages) {
        this.viewer = viewer;
        this.owner = owner;
        this.openPortfolio = openPortfolio;
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

    public Inventory getOpenPortfolio() {
        return openPortfolio;
    }

    public void setOpenPortfolio(Inventory openPortfolio) {
        this.openPortfolio = openPortfolio;
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
        FUND_PORTFOLIO
    }
}
