package com.thedasmc.stocks2.commands.funds;

import co.aikar.commands.annotation.*;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.commands.AbstractStocksCommand;
import com.thedasmc.stocks2.common.Texts;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("stocks")
public class WithdrawFundCommand extends AbstractStocksCommand {

    public WithdrawFundCommand(Stocks2 plugin) {
        super(plugin);
    }

    @Subcommand("fund withdraw|wd")
    @CommandPermission(DepositFundCommand.DEPOSIT_FUND_PERMISSION)
    @Description("Withdraw money from a fund")
    @Syntax("[fundId] [amount]")
    public void onWithdrawFund(Player player, long fundId, double amount) {
        final Texts texts = plugin.getTexts();
        final UUID uuid = player.getUniqueId();

        plugin.getExecutorService().execute(() -> {
            if (transactFund(player, uuid, fundId, amount * -1))
                player.sendMessage(texts.getText(Texts.Types.TRANSACTED_FUND));
        });
    }
}
