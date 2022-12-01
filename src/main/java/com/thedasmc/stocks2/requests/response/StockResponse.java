package com.thedasmc.stocks2.requests.response;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class StockResponse extends StockDataResponse {

    private final BigDecimal shares;
    private final BigInteger centsInvested;
    private final BigInteger centsValue;

    protected StockResponse(StockBuilder builder) {
        super(builder);
        this.shares = builder.shares;
        this.centsInvested = builder.centsInvested;
        this.centsValue = builder.centsValue;
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

        public StockResponse build() {
            return new StockResponse(this);
        }
    }

}
