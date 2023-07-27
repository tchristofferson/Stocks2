package com.thedasmc.stocks2.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.requests.interactors.AbstractFundDataInteractor;
import com.thedasmc.stocks2.requests.request.FundStockRequest;
import com.thedasmc.stocks2.requests.response.FundResponse;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

@CommandAlias("stocks")
public class AddStockToFundCommand extends BaseCommand {

    private final Stocks2 plugin;

    public AddStockToFundCommand(Stocks2 plugin) {
        this.plugin = plugin;
    }

    @Subcommand("fund add")
    @CommandPermission(CreateFundCommand.CREATE_FUND_PERMISSION)
    @Description("Add a stock to a fund")
    @Syntax("[symbol]")
    public void onAddStockToFund(Player player, long fundId, @Single String symbol) {
        final Texts texts = plugin.getTexts();
        final AbstractFundDataInteractor fundDataInteractor = plugin.getFundDataInteractor();
        final UUID uuid = player.getUniqueId();

        plugin.getExecutorService().execute(() -> {
            FundResponse fundResponse;

            try {
                fundResponse = fundDataInteractor.getFund(fundId);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.FUND_FETCH_ERROR, e.getMessage()));
                return;
            }

            if (!fundResponse.getCreatorId().equals(uuid)) {
                player.sendMessage(texts.getText(Texts.Types.NOT_FUND_CREATOR));
                return;
            }

            FundStockRequest fundStockRequest = new FundStockRequest();
            fundStockRequest.setFundId(fundId);
            fundStockRequest.setSymbol(symbol);

            try {
                fundDataInteractor.addStockToFund(fundStockRequest);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.ADD_STOCK_TO_FUND_ERROR, e.getMessage()));
                return;
            }

            player.sendMessage(texts.getText(Texts.Types.ADDED_STOCK_TO_FUND));
        });
    }
}
