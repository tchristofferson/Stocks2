package com.thedasmc.stocks2.commands.funds;

import co.aikar.commands.annotation.*;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.commands.AbstractStocksCommand;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.requests.interactors.AbstractFundDataInteractor;
import com.thedasmc.stocks2.requests.response.FundResponse;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

@CommandAlias("stocks")
public class RemoveStockFromFundCommand extends AbstractStocksCommand {

    public RemoveStockFromFundCommand(Stocks2 plugin) {
        super(plugin);
    }

    @Subcommand("fund remove|rm")
    @CommandPermission(CreateFundCommand.CREATE_FUND_PERMISSION)
    @Description("Remove a stock from a fund")
    @Syntax("[fundId] [symbol]")
    public void onRemoveStockFromFund(Player player, long fundId, @Single String symbol) {
        final Texts texts = plugin.getTexts();
        final AbstractFundDataInteractor fundDataInteractor = plugin.getFundDataInteractor();
        final UUID uuid = player.getUniqueId();

        plugin.getExecutorService().execute(() -> {
            FundResponse fundResponse = getFund(player, fundId);

            if (fundResponse == null)
                return;

            if (isNotFundCreator(player, uuid, fundResponse))
                return;

            try {
                fundDataInteractor.removeStockFromFund(fundId, symbol);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.REMOVE_STOCK_FROM_FUND_ERROR, e.getMessage()));
                return;
            }

            player.sendMessage(texts.getText(Texts.Types.REMOVED_STOCK_FROM_FUND));
        });
    }
}
