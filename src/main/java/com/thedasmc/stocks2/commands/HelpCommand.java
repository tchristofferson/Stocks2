package com.thedasmc.stocks2.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import org.bukkit.command.CommandSender;

@CommandAlias("stocks")
public class HelpCommand extends BaseCommand {

    @co.aikar.commands.annotation.HelpCommand
    public void onHelp(CommandSender commandSender, CommandHelp help) {
        help.showHelp();
    }

}
