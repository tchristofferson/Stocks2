package com.thedasmc.stocks2.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.HelpCommand;
import org.bukkit.command.CommandSender;

@CommandAlias("stocks")
public class StocksHelpCommand extends BaseCommand {

    @HelpCommand
    public void onHelp(CommandSender commandSender, CommandHelp help) {
        help.showHelp();
    }

}
