package com.thedasmc.stocks2.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Constants;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.common.Tools;
import com.thedasmc.stocks2.requests.interactors.AbstractPlayerDataInteractor;
import com.thedasmc.stocks2.requests.request.RecordRequest;
import com.thedasmc.stocks2.requests.response.StockResponse;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@CommandAlias("stocks")
public class BuyCommand extends BaseCommand {

    private static final String BUY_PERMISSION = "stocks.buy";

    private final Stocks2 plugin;

    public BuyCommand(Stocks2 plugin) {
        this.plugin = plugin;
    }

    @Subcommand("buy")
    @CommandPermission(BUY_PERMISSION)
    @Description("Buy shares of a stock")
    @Syntax("[symbol] [shares]")
    public void onBuy(Player player, String symbol, @Conditions(Constants.POSITIVE_SHARE_LIMITS_CONDITION) Double shares) {
        final Texts texts = plugin.getTexts();
        final AbstractPlayerDataInteractor playerDataInteractor = plugin.getPlayerDataInteractor();
        final UUID uuid = player.getUniqueId();

        if (plugin.isStockWhitelistEnabled() && !plugin.isWhitelisted(symbol)) {
            player.sendMessage(texts.getText(Texts.Types.NOT_WHITELISTED));
            return;
        } else if (plugin.isStockBlacklistEnabled() && plugin.isBlacklisted(symbol)) {
            player.sendMessage(texts.getText(Texts.Types.BLACKLISTED));
            return;
        }

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

            final BigDecimal value = stock.getLatestPrice().multiply(BigDecimal.valueOf(shares).setScale(2, RoundingMode.DOWN));

            if (!Tools.canConvertToDouble(value)) {
                player.sendMessage(texts.getText(Texts.Types.SALE_TOO_LARGE));
                return;
            }

            Future<Boolean> futureWithdrawalSuccess = Bukkit.getScheduler().callSyncMethod(plugin, () -> {
                if (!player.isOnline())
                    return false;

                final double valueDouble = value.doubleValue();
                final Economy economy = plugin.getEconomy();

                if (!economy.has(player, valueDouble)) {
                    player.sendMessage(texts.getText(Texts.Types.NOT_ENOUGH_MONEY, value));
                    return false;
                }

                EconomyResponse economyResponse = economy.withdrawPlayer(player, valueDouble);

                if (!economyResponse.transactionSuccess()) {
                    player.sendMessage(texts.getErrorText(Texts.Types.WITHDRAW_FUNDS_ERROR, economyResponse.errorMessage));
                    return false;
                }

                return true;
            });

            boolean fundsWithdrawn;

            try {
                fundsWithdrawn = futureWithdrawalSuccess.get(3, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                Bukkit.getLogger().severe("Exception occurred while trying to withdraw funds from player on main thread!");
                e.printStackTrace();
                return;
            }

            if (!fundsWithdrawn)
                return;

            RecordRequest recordRequest = new RecordRequest(uuid, symbol, Tools.toCents(value), BigDecimal.valueOf(shares));

            try {
                playerDataInteractor.transact(recordRequest);
            } catch (IOException e) {
                //Failed to transact shares, refund money
                Bukkit.getScheduler().runTask(plugin, () -> plugin.getEconomy().depositPlayer(player, value.doubleValue()));
                player.sendMessage(texts.getErrorText(Texts.Types.TRANSACTION_ERROR, e.getMessage()));
                return;
            }

            player.sendMessage(texts.getText(Texts.Types.BOUGHT_SHARES_SUCCESS, value));
        });
    }

}
