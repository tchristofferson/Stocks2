package com.thedasmc.stocks2.requests.models;

import java.math.BigDecimal;
import java.util.Objects;

public class StockData {

    private final String symbol;
    private final String companyName;
    private final String calculationPrice;
    private final BigDecimal open;
    private final BigDecimal close;
    private final BigDecimal latestPrice;
    private final BigDecimal changePercent;
    private final BigDecimal week52High;
    private final BigDecimal week52Low;
    private final Boolean isUSMarketOpen;

    private StockData(StockDataBuilder builder) {
        this.symbol = builder.symbol;
        this.companyName = builder.companyName;
        this.calculationPrice = builder.calculationPrice;
        this.open = builder.open;
        this.close = builder.close;
        this.latestPrice = builder.latestPrice;
        this.changePercent = builder.changePercent;
        this.week52High = builder.week52High;
        this.week52Low = builder.week52Low;
        this.isUSMarketOpen = builder.isUSMarketOpen;
    }

    public static StockDataBuilder newBuilder() {
        return new StockDataBuilder();
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCalculationPrice() {
        return calculationPrice;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getClose() {
        return close;
    }

    public BigDecimal getLatestPrice() {
        return latestPrice;
    }

    public BigDecimal getChangePercent() {
        return changePercent;
    }

    public BigDecimal getWeek52High() {
        return week52High;
    }

    public BigDecimal getWeek52Low() {
        return week52Low;
    }

    public Boolean isUSMarketOpen() {
        return isUSMarketOpen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockData stockData = (StockData) o;
        return Objects.equals(symbol, stockData.symbol) && Objects.equals(companyName, stockData.companyName) && Objects.equals(calculationPrice, stockData.calculationPrice) && Objects.equals(open, stockData.open) && Objects.equals(close, stockData.close) && Objects.equals(latestPrice, stockData.latestPrice) && Objects.equals(changePercent, stockData.changePercent) && Objects.equals(week52High, stockData.week52High) && Objects.equals(week52Low, stockData.week52Low) && Objects.equals(isUSMarketOpen, stockData.isUSMarketOpen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, companyName, calculationPrice, open, close, latestPrice, changePercent, week52High, week52Low, isUSMarketOpen);
    }

    public static class StockDataBuilder {

        private String symbol;
        private String companyName;
        private String calculationPrice;
        private BigDecimal open;
        private BigDecimal close;
        private BigDecimal latestPrice;
        private BigDecimal changePercent;
        private BigDecimal week52High;
        private BigDecimal week52Low;
        private Boolean isUSMarketOpen;

        private StockDataBuilder() {}

        public String getSymbol() {
            return symbol;
        }

        public StockDataBuilder withSymbol(String symbol) {
            if (this.symbol != null)
                throw new IllegalStateException("symbol already set!");

            this.symbol = symbol;
            return this;
        }

        public String getCompanyName() {
            return companyName;
        }

        public StockDataBuilder withCompanyName(String companyName) {
            if (this.companyName != null)
                throw new IllegalStateException("companyName already set!");

            this.companyName = companyName;
            return this;
        }

        public String getCalculationPrice() {
            return calculationPrice;
        }

        public StockDataBuilder withCalculationPrice(String calculationPrice) {
            if (this.calculationPrice != null)
                throw new IllegalStateException("calculationPrice already set!");

            this.calculationPrice = calculationPrice;
            return this;
        }

        public BigDecimal getOpen() {
            return open;
        }

        public StockDataBuilder withOpen(BigDecimal open) {
            if (this.open != null)
                throw new IllegalStateException("open already set!");

            this.open = open;
            return this;
        }

        public BigDecimal getClose() {
            return close;
        }

        public StockDataBuilder withClose(BigDecimal close) {
            if (this.close != null)
                throw new IllegalStateException("close already set!");

            this.close = close;
            return this;
        }

        public BigDecimal getLatestPrice() {
            return latestPrice;
        }

        public StockDataBuilder withLatestPrice(BigDecimal latestPrice) {
            if (this.latestPrice != null)
                throw new IllegalStateException("latestPrice already set!");

            this.latestPrice = latestPrice;
            return this;
        }

        public BigDecimal getChangePercent() {
            return changePercent;
        }

        public StockDataBuilder withChangePercent(BigDecimal changePercent) {
            if (this.changePercent != null)
                throw new IllegalStateException("changePercent already set!");

            this.changePercent = changePercent;
            return this;
        }

        public BigDecimal getWeek52High() {
            return week52High;
        }

        public StockDataBuilder withWeek52High(BigDecimal week52High) {
            if (this.week52High != null)
                throw new IllegalStateException("week52High already set!");

            this.week52High = week52High;
            return this;
        }

        public BigDecimal getWeek52Low() {
            return week52Low;
        }

        public StockDataBuilder withWeek52Low(BigDecimal week52Low) {
            if (this.week52Low != null)
                throw new IllegalStateException("week52Low already set!");

            this.week52Low = week52Low;
            return this;
        }

        public Boolean getUSMarketOpen() {
            return isUSMarketOpen;
        }

        public StockDataBuilder withUSMarketOpen(Boolean isUSMarketOpen) {
            if (this.isUSMarketOpen != null)
                throw new IllegalStateException("isUSMarketOpen already set!");

            this.isUSMarketOpen = isUSMarketOpen;
            return this;
        }

        public StockData build() {
            return new StockData(this);
        }
    }
}
