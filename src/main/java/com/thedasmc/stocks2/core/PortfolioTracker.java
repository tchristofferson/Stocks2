package com.thedasmc.stocks2.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PortfolioTracker {

    //The key is the UUID of the viewer
    private final Map<UUID, PortfolioViewer> portfolioTackerMap = new HashMap<>();

    public Map<UUID, PortfolioViewer> getPortfolioTackerMap() {
        return new HashMap<>(portfolioTackerMap);
    }

    public PortfolioViewer getViewer(UUID viewer) {
        return portfolioTackerMap.get(viewer);
    }

    public void track(PortfolioViewer portfolioViewer) {
        portfolioTackerMap.put(portfolioViewer.getViewer(), portfolioViewer);
    }

    public void untrack(UUID viewer) {
        portfolioTackerMap.remove(viewer);
    }
}
