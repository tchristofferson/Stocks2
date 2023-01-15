package com.thedasmc.stocks2.common;

import com.thedasmc.stocks2.Stocks2;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.util.Objects;

public class Texts {

    private final Stocks2 plugin;

    public Texts(Stocks2 plugin) {
        this.plugin = plugin;
    }

    public String getText(Types type) {
        String rawText = plugin.getTextsConfig().getString(type.key);
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(rawText));
    }

    public String getText(Types type, BigDecimal money) {
        return getText(type).replace("%money%", getMoneySymbol() + money.toPlainString());
    }

    public String getText(Types type, BigDecimal money, String symbol) {
        return getText(type, money).replace("%symbol%", symbol.trim().toUpperCase());
    }

    public String getErrorText(Types type, String error) {
        return getText(type).replace("%error%", error);
    }

    private char getMoneySymbol() {
        return plugin.getConfig().getString("money-symbol", "$").charAt(0);
    }

    public enum Types {
        FETCH_PORTFOLIO_ERROR("portfolio-error"),
        STOCK_FETCH_ERROR("stock-error"),
        NOT_ENOUGH_SHARES("not-enough-shares"),
        NOT_ENOUGH_MONEY("not-enough-money"),
        SALE_TOO_LARGE("sale-too-large"),
        STOCK_SYMBOL_NOT_FOUND("stock-symbol-not-found"),
        DEPOSIT_FUNDS_ERROR("deposit-funds-error"),
        WITHDRAW_FUNDS_ERROR("withdraw-funds-error"),
        TRANSACTION_ERROR("transaction-error"),
        TRANSACTION_CANCEL_ERROR("transaction-cancel-error"),
        ACCOUNT_REGISTRATION_ERROR("account-registration-error"),
        SOLD_SHARES_SUCCESS("sold-shares-success"),
        BOUGHT_SHARES_SUCCESS("bought-shares-success"),
        CHECK_PRICE_SUCCESS("check-price-success"),
        ACCOUNT_REGISTRATION_SUCCESS("account-registration-success");

        //The key in texts.yml
        private final String key;

        Types(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
