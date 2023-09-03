package com.thedasmc.stocks2.requests.response;

import java.util.List;

public class FundsByCreatorResponse extends AbstractPageResponse {

    private List<FundResponse> funds;

    public List<FundResponse> getFunds() {
        return funds;
    }

    public void setFunds(List<FundResponse> funds) {
        this.funds = funds;
    }
}
