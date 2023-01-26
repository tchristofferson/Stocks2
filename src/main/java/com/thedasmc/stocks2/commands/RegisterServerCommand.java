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
public class RegisterServerCommand extends BaseCommand {

    private static final String REGISTER_SERVER_PERMISSION = "stocks.admin.account.register";

    private final Stocks2 plugin;

    public RegisterServerCommand(Stocks2 plugin) {
        this.plugin = plugin;
    }

    @Subcommand("regser|rs")
    @CommandPermission(REGISTER_SERVER_PERMISSION)
    @Description("Register a server")
    public void onRegister(CommandSender commandSender, String email, String password) {
        final Texts texts = plugin.getTexts();
        final AccountRequest request = new AccountRequest(email, password);

        plugin.getExecutorService().execute(() -> {
            try {
                plugin.getServerInteractor().registerServer(request);
            } catch (IOException e) {
                commandSender.sendMessage(texts.getErrorText(Texts.Types.SERVER_REGISTRATION_ERROR, e.getMessage()));
                return;
            }

            commandSender.sendMessage(texts.getText(Texts.Types.SERVER_REGISTRATION_SUCCESS));
        });
    }

}
