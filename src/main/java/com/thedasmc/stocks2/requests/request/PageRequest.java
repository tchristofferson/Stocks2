package com.thedasmc.stocks2.requests.request;

public class PageRequest extends BaseRequest {

    private final int page;
    private final int pageLimit;

    public PageRequest(int page, int pageLimit) {
        this.page = page;
        this.pageLimit = pageLimit;
    }

    public int getPage() {
        return page;
    }

    public int getPageLimit() {
        return pageLimit;
    }
}
