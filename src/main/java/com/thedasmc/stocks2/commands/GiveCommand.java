package com.thedasmc.stocks2.commands;

import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Constants;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.requests.interactors.AbstractPlayerDataInteractor;
import com.thedasmc.stocks2.requests.request.RecordRequest;
import com.thedasmc.stocks2.requests.response.RecordResponse;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@CommandAlias("stocks")
public class GiveCommand extends AbstractStocksCommand {

    private static final String GIVE_PERMISSION = "stocks.give";

    public GiveCommand(Stocks2 plugin) {
        super(plugin);
    }

    @Subcommand("give")
    @CommandPermission(GIVE_PERMISSION)
    @Description("Give a player shares of a stock")
    @Syntax("[player] [symbol] [shares]")
    public void onGive(CommandSender commandSender, OnlinePlayer onlinePlayer, String symbol, @Conditions(Constants.POSITIVE_SHARE_LIMITS_CONDITION) Double shares) {
        final Texts texts = plugin.getTexts();
        final AbstractPlayerDataInteractor playerDataInteractor = plugin.getPlayerDataInteractor();
        final UUID uuid = onlinePlayer.getPlayer().getUniqueId();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            RecordRequest recordRequest = new RecordRequest(uuid, symbol, BigInteger.ZERO, BigDecimal.valueOf(shares));
            RecordResponse recordResponse;

            try {
                recordResponse = playerDataInteractor.transact(recordRequest);
            } catch (IOException e) {
                commandSender.sendMessage(texts.getErrorText(Texts.Types.TRANSACTION_ERROR, e.getMessage()));
                return;
            }

            commandSender.sendMessage(
                texts.getText(Texts.Types.GAVE_SHARES, onlinePlayer.getPlayer().getName(),
                    recordResponse.getShares(), recordResponse.getSymbol())
            );

            onlinePlayer.getPlayer().sendMessage(
                texts.getText(Texts.Types.RECEIVED_SHARES, "", recordResponse.getShares(), recordResponse.getSymbol()));
        });
    }

}
