package com.thedasmc.stocks2.commands.funds;

import co.aikar.commands.annotation.*;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.commands.AbstractStocksCommand;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.requests.interactors.AbstractFundDataInteractor;
import com.thedasmc.stocks2.requests.request.CreateFundRequest;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

@CommandAlias("stocks")
public class CreateFundCommand extends AbstractStocksCommand {

    public static final String CREATE_FUND_PERMISSION = "stocks.fund.create";

    public CreateFundCommand(Stocks2 plugin) {
        super(plugin);
    }

    @Subcommand("fund create")
    @CommandPermission(CREATE_FUND_PERMISSION)
    @Description("Create a stock fund")
    @Syntax("[fundName]")
    public void onCreateFund(Player player, String fundName) {
        final Texts texts = plugin.getTexts();
        final AbstractFundDataInteractor fundDataInteractor = plugin.getFundDataInteractor();
        final UUID uuid = player.getUniqueId();

        plugin.getExecutorService().execute(() -> {
            CreateFundRequest request = new CreateFundRequest();
            request.setCreatorId(uuid);
            request.setName(fundName);

            try {
                fundDataInteractor.createFund(request);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.FUND_CREATION_ERROR, e.getMessage()));
                return;
            }

            player.sendMessage(texts.getText(Texts.Types.FUND_CREATED));
        });
    }
}
