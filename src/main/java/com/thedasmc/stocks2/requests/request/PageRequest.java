package com.thedasmc.stocks2.requests.request;

public class PageRequest extends BaseRequest {

    private final int page;

    public PageRequest(String apiToken, int page) {
        super(apiToken);
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
