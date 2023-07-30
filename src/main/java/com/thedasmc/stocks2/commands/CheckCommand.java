package com.thedasmc.stocks2.commands;

import co.aikar.commands.annotation.*;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.requests.response.StockDataResponse;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@CommandAlias("stocks")
public class CheckCommand extends AbstractStocksCommand {

    private static final String CHECK_PERMISSION = "stocks.check";

    public CheckCommand(Stocks2 plugin) {
        super(plugin);
    }

    @Subcommand("check")
    @CommandPermission(CHECK_PERMISSION)
    @Description("Check the price of a stock")
    @Syntax("[symbol]")
    public void onCheck(CommandSender commandSender, String symbol) {
        final String finalSymbol = symbol.trim().toUpperCase();
        final Texts texts = plugin.getTexts();

        plugin.getExecutorService().execute(() -> {
            Map<String, StockDataResponse> stockData;

            try {
                stockData = plugin.getStockDataInteractor().getQuotes(Collections.singletonList(finalSymbol));
            } catch (IOException e) {
                commandSender.sendMessage(texts.getErrorText(Texts.Types.STOCK_FETCH_ERROR, e.getMessage()));
                return;
            }

            if (stockData.get(finalSymbol) == null) {
                commandSender.sendMessage(texts.getText(Texts.Types.STOCK_SYMBOL_NOT_FOUND));
                return;
            }

            StockDataResponse stockDataResponse = stockData.get(finalSymbol);
            commandSender.sendMessage(texts.getText(Texts.Types.CHECK_PRICE_SUCCESS, stockDataResponse.getLatestPrice(), finalSymbol));
        });
    }
}
