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
import com.thedasmc.stocks2.requests.request.DeleteRecordsRequest;
import com.thedasmc.stocks2.requests.response.ShareSummaryResponse;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@CommandAlias("stocks")
public class SellAllCommand extends BaseCommand {

    private final Stocks2 plugin;

    public SellAllCommand(Stocks2 plugin) {
        this.plugin = plugin;
    }

    @Subcommand("sell all")
    @CommandPermission(SellCommand.SELL_PERMISSION)
    @Description("Sell all stocks")
    public void onSellAll(Player player) {
        final Texts texts = plugin.getTexts();
        final AbstractPlayerDataInteractor playerDataInteractor = plugin.getPlayerDataInteractor();
        final UUID uuid = player.getUniqueId();

        plugin.getExecutorService().execute(() -> {
            List<ShareSummaryResponse> shareSummaries;

            try {
                shareSummaries = playerDataInteractor.getShareSummaries(uuid);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.TRANSACTION_ERROR, e.getMessage()));
                return;
            }

            if (shareSummaries.isEmpty()) {
                player.sendMessage(texts.getText(Texts.Types.NOT_ENOUGH_SHARES));
                return;
            }

            Duration cooldownDuration = plugin.getTradeCooldownDuration();
            //filter out stocks with cooldown
            shareSummaries = shareSummaries.stream()
                .filter(shareSummaryResponse -> !shareSummaryResponse.hasCooldown(cooldownDuration))
                .collect(Collectors.toList());

            final BigInteger centsValue = shareSummaries.stream()
                .map(ShareSummaryResponse::getCentsValue)
                .reduce(BigInteger.ZERO, BigInteger::add);

            BigDecimal value = new BigDecimal(centsValue)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN);

            if (!Tools.canConvertToDouble(value)) {
                player.sendMessage(texts.getText(Texts.Types.SALE_TOO_LARGE));
                return;
            }

            Future<EconomyResponse> futureEconomyResponse = Bukkit.getScheduler()
                .callSyncMethod(plugin, () -> plugin.getEconomy().depositPlayer(player, value.doubleValue()));

            EconomyResponse economyResponse = null;
            boolean depositedFunds;
            String errorMessage;

            try {
                economyResponse = futureEconomyResponse.get();
                depositedFunds = economyResponse.transactionSuccess();
                errorMessage = economyResponse.errorMessage;
            } catch (ExecutionException | InterruptedException e) {
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

            Map<String, BigDecimal> stocks = shareSummaries.stream()
                .collect(Collectors.toMap(ShareSummaryResponse::getSymbol, ShareSummaryResponse::getShares));

            DeleteRecordsRequest deleteRecordsRequest = new DeleteRecordsRequest(uuid, stocks);

            try {
                playerDataInteractor.deleteRecords(deleteRecordsRequest);
            } catch (IOException e) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    plugin.getEconomy().withdrawPlayer(player, value.doubleValue());
                    player.sendMessage(texts.getErrorText(Texts.Types.TRANSACTION_ERROR, e.getMessage()));
                });
                return;
            }

            player.sendMessage(texts.getText(Texts.Types.SOLD_ALL_SHARES_SUCCESS, value));
        });
    }
}
