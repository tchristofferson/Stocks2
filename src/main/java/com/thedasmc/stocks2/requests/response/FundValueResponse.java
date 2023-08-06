package com.thedasmc.stocks2.requests.response;

import java.math.BigInteger;

public class FundValueResponse {

    private BigInteger centsValue;

    public BigInteger getCentsValue() {
        return centsValue;
    }

    public void setCentsValue(BigInteger centsValue) {
        this.centsValue = centsValue;
    }
}
