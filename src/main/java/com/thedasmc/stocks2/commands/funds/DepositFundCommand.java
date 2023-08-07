package com.thedasmc.stocks2.commands.funds;

import co.aikar.commands.annotation.*;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.commands.AbstractStocksCommand;
import com.thedasmc.stocks2.common.Texts;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("stocks")
public class DepositFundCommand extends AbstractStocksCommand {

    public static final String DEPOSIT_FUND_PERMISSION = "stocks.fund.deposit";

    public DepositFundCommand(Stocks2 plugin) {
        super(plugin);
    }

    @Subcommand("fund deposit|dep")
    @CommandPermission(DEPOSIT_FUND_PERMISSION)
    @Description("Deposit money into a fund")
    @Syntax("[fundId] [amount]")
    public void onFundDeposit(Player player, long fundId, double amount) {
        final Texts texts = plugin.getTexts();
        final UUID uuid = player.getUniqueId();

        plugin.getExecutorService().execute(() -> {
            if (transactFund(player, uuid, fundId, amount))
                player.sendMessage(texts.getText(Texts.Types.TRANSACTED_FUND));
        });
    }
}
