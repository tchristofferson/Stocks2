package com.thedasmc.stocks2.core;

import java.util.UUID;

public class PortfolioViewer {

    private UUID viewer;
    private UUID owner;
    private int page;

    public PortfolioViewer(UUID viewer, UUID owner, int page) {
        this.viewer = viewer;
        this.owner = owner;
        this.page = page;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
