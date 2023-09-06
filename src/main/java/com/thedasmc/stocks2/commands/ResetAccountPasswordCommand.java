package com.thedasmc.stocks2.commands;

import co.aikar.commands.annotation.*;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import org.bukkit.command.CommandSender;

import java.io.IOException;

@CommandAlias("stocks")
public class ResetAccountPasswordCommand extends AbstractStocksCommand {

    public ResetAccountPasswordCommand(Stocks2 plugin) {
        super(plugin);
    }

    @Subcommand("respas|respass")
    @CommandPermission(RegisterAccountCommand.REGISTER_ACCOUNT_PERMISSION)
    @Description("Reset your account password. You will receive email")
    @Syntax("[email]")
    public void onResetAccountPassword(CommandSender sender, String email) {
        final Texts texts = plugin.getTexts();

        plugin.getExecutorService().execute(() -> {
            try {
                plugin.getAccountInteractor().requestAccountPasswordReset(email);
            } catch (IOException e) {
                sender.sendMessage(texts.getErrorText(Texts.Types.PASSWORD_RESET_ERROR, e.getMessage()));
                return;
            }

            sender.sendMessage(texts.getText(Texts.Types.RESET_PASSWORD_SUCCESS));
        });
    }
}
