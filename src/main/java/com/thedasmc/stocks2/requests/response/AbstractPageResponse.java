package com.thedasmc.stocks2.requests.response;

public class AbstractPageResponse {

    private Integer page;
    private Integer pages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
}
