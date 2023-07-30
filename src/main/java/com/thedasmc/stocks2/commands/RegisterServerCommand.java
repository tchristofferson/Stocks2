package com.thedasmc.stocks2.commands;

import co.aikar.commands.annotation.*;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.requests.request.AccountRequest;
import com.thedasmc.stocks2.requests.response.ServerRegistrationResponse;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.IOException;

@CommandAlias("stocks")
public class RegisterServerCommand extends AbstractStocksCommand {

    public static final String COMMAND = "regser";
    public static final String COMMAND_ALIAS = "rs";
    private static final String REGISTER_SERVER_PERMISSION = "stocks.admin.account.register";

    public RegisterServerCommand(Stocks2 plugin) {
        super(plugin);
    }

    @Subcommand(COMMAND + "|" + COMMAND_ALIAS)
    @CommandPermission(REGISTER_SERVER_PERMISSION)
    @Description("Register a server")
    @Syntax("[email] [password]")
    public void onRegister(CommandSender commandSender, String email, String password) {
        final Texts texts = plugin.getTexts();
        final AccountRequest request = new AccountRequest(email, password);

        plugin.getExecutorService().execute(() -> {
            ServerRegistrationResponse response;

            try {
                response = plugin.getServerInteractor().registerServer(request);
            } catch (IOException e) {
                commandSender.sendMessage(texts.getErrorText(Texts.Types.SERVER_REGISTRATION_ERROR, e.getMessage()));
                return;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                plugin.getConfig().set("api-token", response.getApiToken());
                plugin.saveConfig();
                commandSender.sendMessage(texts.getText(Texts.Types.SERVER_REGISTRATION_SUCCESS));
            });
        });
    }

}
