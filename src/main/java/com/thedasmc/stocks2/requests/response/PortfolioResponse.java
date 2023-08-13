package com.thedasmc.stocks2.requests.response;

import java.util.List;

public class PortfolioResponse extends AbstractPageResponse {

    private final List<StockResponse> stocks;

    public PortfolioResponse(List<StockResponse> stocks, int page, int pages) {
        this.stocks = stocks;
        setPage(page);
        setPages(pages);
    }

    public List<StockResponse> getStocks() {
        return stocks;
    }

}
