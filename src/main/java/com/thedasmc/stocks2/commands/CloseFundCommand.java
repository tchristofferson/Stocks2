package com.thedasmc.stocks2.commands;

import co.aikar.commands.annotation.*;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("stocks")
public class CloseFundCommand extends AbstractStocksCommand {

    public CloseFundCommand(Stocks2 plugin) {
        super(plugin);
    }

    @Subcommand("fund close")
    @CommandPermission(CreateFundCommand.CREATE_FUND_PERMISSION)
    @Description("Close a fund, player's won't be able to deposit only withdraw")
    @Syntax("[fundId]")
    public void onCloseFund(Player player, long fundId) {
        final UUID uuid = player.getUniqueId();
        final Texts texts = plugin.getTexts();

        plugin.getExecutorService().execute(() -> {
            if (updateFundStatus(player, uuid, fundId, 'C'))
                player.sendMessage(texts.getText(Texts.Types.CLOSED_FUND));
        });
    }
}
