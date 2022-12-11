package com.thedasmc.stocks2.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.common.Tools;
import com.thedasmc.stocks2.requests.AbstractPlayerDataInteractor;
import com.thedasmc.stocks2.requests.request.RecordRequest;
import com.thedasmc.stocks2.requests.response.StockResponse;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@CommandAlias("stocks")
public class SellCommand extends BaseCommand {

    private static final String SELL_PERMISSION = "stocks.sell";

    private final Stocks2 plugin;

    public SellCommand(Stocks2 plugin) {
        this.plugin = plugin;
    }

    @Subcommand("sell")
    @CommandPermission(SELL_PERMISSION)
    @Description("Sell shares of a stock")
    public void onSell(Player player, String symbol, double shares) {
        final Texts texts = plugin.getTexts();
        final AbstractPlayerDataInteractor playerDataRequester = plugin.getPlayerDataRequester();
        final UUID uuid = player.getUniqueId();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            StockResponse stock;

            try {
                stock = playerDataRequester.getStock(uuid, symbol);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.STOCK_FETCH_ERROR, e.getMessage()));
                return;
            }

            if (stock.getShares().compareTo(BigDecimal.valueOf(shares)) < 0) {
                player.sendMessage(texts.getText(Texts.Types.NOT_ENOUGH_SHARES));
                return;
            }

            BigDecimal value = stock.getLatestPrice().multiply(BigDecimal.valueOf(shares)).setScale(2, RoundingMode.DOWN);

            //Check if the value can be converted to double
            if (!Tools.canConvertToDouble(value)) {
                player.sendMessage(texts.getText(Texts.Types.SALE_TOO_LARGE));
                return;
            }

            RecordRequest recordRequest = new RecordRequest(uuid, symbol, Tools.toCents(value), BigDecimal.valueOf(shares));

            try {
                playerDataRequester.transact(recordRequest);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.TRANSACTION_ERROR, e.getMessage()));
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                if (!player.isOnline())
                    return;

                EconomyResponse response = plugin.getEconomy().depositPlayer(player, value.doubleValue());

                if (!response.transactionSuccess()) {
                    player.sendMessage(texts.getErrorText(Texts.Types.DEPOSIT_FUNDS_ERROR, response.errorMessage));

                    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                        //TODO: Call cancel transaction/record endpoint to take back the shares
                    });

                    return;
                }

                //TODO: Send player a success message
            });
        });
    }
}
