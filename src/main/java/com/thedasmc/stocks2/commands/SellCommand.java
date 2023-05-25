package com.thedasmc.stocks2.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Constants;
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
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@CommandAlias("stocks")
public class SellCommand extends BaseCommand {

    public static final String SELL_PERMISSION = "stocks.sell";

    private final Stocks2 plugin;

    public SellCommand(Stocks2 plugin) {
        this.plugin = plugin;
    }

    @Subcommand("sell")
    @CommandPermission(SELL_PERMISSION)
    @Description("Sell shares of a stock")
    @Syntax("[symbol] [shares]")
    public void onSell(Player player, String symbol, @Conditions(Constants.POSITIVE_SHARE_LIMITS_CONDITION) Double shares) {
        final Texts texts = plugin.getTexts();
        final AbstractPlayerDataInteractor playerDataInteractor = plugin.getPlayerDataInteractor();
        final UUID uuid = player.getUniqueId();

        plugin.getExecutorService().execute(() -> {
            StockResponse stock;

            try {
                stock = playerDataInteractor.getStock(uuid, symbol);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.STOCK_FETCH_ERROR, e.getMessage()));
                return;
            }

            if (stock == null) {
                player.sendMessage(texts.getText(Texts.Types.STOCK_SYMBOL_NOT_FOUND));
                return;
            }

            if (stock.getShares().compareTo(BigDecimal.valueOf(shares)) < 0) {
                player.sendMessage(texts.getText(Texts.Types.NOT_ENOUGH_SHARES));
                return;
            }

            if (stock.getLastPurchaseTime() != null) {
                Instant now = Instant.now();
                Instant lastPurchaseTime = Instant.ofEpochMilli(stock.getLastPurchaseTime());
                Instant expire = lastPurchaseTime.plus(plugin.getTradeCooldown(), ChronoUnit.SECONDS);

                Duration duration = Duration.between(now, lastPurchaseTime);
                long seconds = duration.getSeconds();

                if (seconds < plugin.getTradeCooldown()) {
                    player.sendMessage(texts.getDurationText(Texts.Types.COOLDOWN, Duration.between(now, expire)));
                    return;
                }
            }

            BigDecimal value = stock.getLatestPrice().multiply(BigDecimal.valueOf(shares)).setScale(2, RoundingMode.DOWN);

            //Check if the value can be converted to double
            if (!Tools.canConvertToDouble(value)) {
                player.sendMessage(texts.getText(Texts.Types.SALE_TOO_LARGE));
                return;
            }

            Future<EconomyResponse> futureEconomyResponse = Bukkit.getScheduler().callSyncMethod(plugin, () ->
                plugin.getEconomy().depositPlayer(player, value.doubleValue()));

            EconomyResponse economyResponse = null;
            boolean depositedFunds;
            String errorMessage;

            try {
                economyResponse = futureEconomyResponse.get();
                depositedFunds = economyResponse.transactionSuccess();
                errorMessage = economyResponse.errorMessage;
            } catch (InterruptedException | ExecutionException e) {
                if (e instanceof ExecutionException) {
                    errorMessage = e.getCause().getMessage();
                } else {
                    errorMessage = e.getMessage();
                }

                depositedFunds = false;
            }

            if (!depositedFunds) {
                player.sendMessage(texts.getErrorText(Texts.Types.DEPOSIT_FUNDS_ERROR, economyResponse == null ? errorMessage : economyResponse.errorMessage));
                return;
            }

            RecordRequest recordRequest = new RecordRequest(uuid, symbol, Tools.toCents(value).negate(), BigDecimal.valueOf(shares).negate());

            try {
                playerDataInteractor.transact(recordRequest);
            } catch (IOException e) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    plugin.getEconomy().withdrawPlayer(player, value.doubleValue());
                    player.sendMessage(texts.getErrorText(Texts.Types.TRANSACTION_ERROR, e.getMessage()));
                });
                return;
            }

            player.sendMessage(texts.getText(Texts.Types.SOLD_SHARES_SUCCESS, value));
        });
    }
}
