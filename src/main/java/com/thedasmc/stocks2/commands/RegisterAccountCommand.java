package com.thedasmc.stocks2.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.requests.request.AccountRequest;
import org.bukkit.command.CommandSender;

import java.io.IOException;

@CommandAlias("stocks")
public class RegisterAccountCommand extends BaseCommand {

    public static final String COMMAND = "regacc";
    public static final String COMMAND_ALIAS = "ra";
    private static final String REGISTER_ACCOUNT_PERMISSION = "stocks.admin.account.register";

    private final Stocks2 plugin;

    public RegisterAccountCommand(Stocks2 plugin) {
        this.plugin = plugin;
    }

    @Subcommand(COMMAND + "|" + COMMAND_ALIAS)
    @CommandPermission(REGISTER_ACCOUNT_PERMISSION)
    @Description("Register an account")
    @Syntax("[email] [password]")
    public void onRegister(CommandSender commandSender, String email, String password) {
        final Texts texts = plugin.getTexts();
        final AccountRequest request = new AccountRequest(email, password);

        plugin.getExecutorService().execute(() -> {
            try {
                plugin.getAccountInteractor().registerAccount(request);
            } catch (IOException e) {
                commandSender.sendMessage(texts.getErrorText(Texts.Types.ACCOUNT_REGISTRATION_ERROR, e.getMessage()));
                return;
            }

            commandSender.sendMessage(texts.getText(Texts.Types.ACCOUNT_REGISTRATION_SUCCESS));
        });
    }

}
