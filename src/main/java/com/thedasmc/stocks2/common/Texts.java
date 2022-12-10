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
        ERROR_FETCHING_PORTFOLIO("portfolio-error");

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
