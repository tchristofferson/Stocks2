package com.thedasmc.stocks2.requests.response;

import com.thedasmc.stocks2.common.Cooldownable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class StockResponse extends StockDataResponse implements Cooldownable {

    private final BigDecimal shares;
    private final BigInteger centsInvested;
    private final BigInteger centsValue;
    private final Long lastPurchaseTime;

    protected StockResponse(StockBuilder builder) {
        super(builder);
        this.shares = builder.shares;
        this.centsInvested = builder.centsInvested;
        this.centsValue = builder.centsValue;
        this.lastPurchaseTime = builder.lastPurchaseTime;
    }

    public BigDecimal getShares() {
        return shares;
    }

    public BigInteger getCentsInvested() {
        return centsInvested;
    }

    public BigInteger getCentsValue() {
        return centsValue;
    }

    @Override
    public Long getLastPurchaseTime() {
        return lastPurchaseTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StockResponse stockResponse = (StockResponse) o;
        return Objects.equals(shares, stockResponse.shares) && Objects.equals(centsInvested, stockResponse.centsInvested) && Objects.equals(centsValue, stockResponse.centsValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), shares, centsInvested, centsValue);
    }

    public static class StockBuilder extends StockDataResponse.StockDataBuilder {

        private BigDecimal shares;
        private BigInteger centsInvested;
        private BigInteger centsValue;
        private Long lastPurchaseTime;

        public BigDecimal getShares() {
            return shares;
        }

        public StockBuilder withShares(BigDecimal shares) {
            if (this.shares != null)
                throw new IllegalStateException("shares already set!");

            this.shares = shares;
            return this;
        }

        public BigInteger getCentsInvested() {
            return centsInvested;
        }

        public StockBuilder withCentsInvested(BigInteger centsInvested) {
            if (this.centsInvested != null)
                throw new IllegalStateException("centsInvested already set!");

            this.centsInvested = centsInvested;
            return this;
        }

        public BigInteger getCentsValue() {
            return centsValue;
        }

        public StockBuilder withCentsValue(BigInteger centsValue) {
            if (this.centsValue != null)
                throw new IllegalStateException("centsValue already set!");

            this.centsValue = centsValue;
            return this;
        }

        public Long getLastPurchaseTime() {
            return lastPurchaseTime;
        }

        public StockBuilder withLastPurchaseTime(Long lastPurchaseTime) {
            if (this.lastPurchaseTime != null)
                throw new IllegalStateException("lastPurchaseTime already set!");

            this.lastPurchaseTime = lastPurchaseTime;
            return this;
        }

        public StockResponse build() {
            return new StockResponse(this);
        }
    }

}
