package com.thedasmc.stocks2.common;

import com.thedasmc.stocks2.Stocks2;
import org.bukkit.ChatColor;

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

    public String getErrorText(Types type, String error) {
        return getText(type).replace("%error%", error);
    }

    public enum Types {
        FETCH_PORTFOLIO_ERROR("portfolio-error"),
        STOCK_FETCH_ERROR("stock-error"),
        NOT_ENOUGH_SHARES("not-enough-shares"),
        SALE_TOO_LARGE("sale-too-large"),
        DEPOSIT_FUNDS_ERROR("deposit-funds-error"),
        TRANSACTION_ERROR("transaction-error");

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
