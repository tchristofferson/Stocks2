package com.thedasmc.stocks2.requests.response;

import java.util.List;

public class PortfolioResponse {

    private final List<StockResponse> stocks;
    private final int pages;
    private final int page;

    public PortfolioResponse(List<StockResponse> stocks, int page, int pages) {
        this.stocks = stocks;
        this.page = page;
        this.pages = pages;
    }

    public List<StockResponse> getStocks() {
        return stocks;
    }

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }
}
