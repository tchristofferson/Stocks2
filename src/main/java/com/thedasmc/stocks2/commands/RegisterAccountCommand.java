package com.thedasmc.stocks2.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.requests.request.AccountRequest;
import org.bukkit.command.CommandSender;

import java.io.IOException;

@CommandAlias("stocks")
public class RegisterAccountCommand extends BaseCommand {

    private static final String REGISTER_ACCOUNT_PERMISSION = "stocks.admin.account.register";

    private final Stocks2 plugin;

    public RegisterAccountCommand(Stocks2 plugin) {
        this.plugin = plugin;
    }

    @Subcommand("regacc|ra")
    @CommandPermission(REGISTER_ACCOUNT_PERMISSION)
    @Description("Register an account")
    public void onRegister(CommandSender commandSender, String email, String password) {
        final Texts texts = plugin.getTexts();
        final AccountRequest request = new AccountRequest(email, password);

        plugin.getExecutorService().submit(() -> {
            try {
                plugin.getAccountInteractor().registerAccount(request);
                System.out.println("registered");
            } catch (IOException e) {
                System.out.println("failed to register");
                commandSender.sendMessage(texts.getErrorText(Texts.Types.ACCOUNT_REGISTRATION_ERROR, e.getMessage()));
                return;
            }

            commandSender.sendMessage(texts.getText(Texts.Types.ACCOUNT_REGISTRATION_SUCCESS));
        });
    }

}
