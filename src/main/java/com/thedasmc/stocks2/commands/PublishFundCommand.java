package com.thedasmc.stocks2.commands;


import co.aikar.commands.annotation.*;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("stocks")
public class PublishFundCommand extends AbstractStocksCommand {

    public PublishFundCommand(Stocks2 plugin) {
        super(plugin);
    }

    @Subcommand("fund publish")
    @CommandPermission(CreateFundCommand.CREATE_FUND_PERMISSION)
    @Description("Publish the fund so other player's can see it")
    @Syntax("[fundId]")
    public void onPublishFund(Player player, long fundId) {
        final UUID uuid = player.getUniqueId();
        final Texts texts = plugin.getTexts();

        plugin.getExecutorService().execute(() -> {
            if (updateFundStatus(player, uuid, fundId, 'O'))
                player.sendMessage(texts.getText(Texts.Types.PUBLISHED_FUND));
        });
    }
}
