package com.thedasmc.stocks2.commands.funds;

import co.aikar.commands.annotation.*;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.commands.AbstractStocksCommand;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.gui.GuiFactory;
import com.thedasmc.stocks2.gui.PortfolioViewer;
import com.thedasmc.stocks2.requests.response.FundPortfolioResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.UUID;

@CommandAlias("stocks")
public class FundPortfolioCommand extends AbstractStocksCommand {

    private static final String FUND_PORTFOLIO_PERMISSION = "stocks.fund.portfolio";

    public FundPortfolioCommand(Stocks2 plugin) {
        super(plugin);
    }

    @Subcommand("fund portfolio")
    @CommandPermission(FUND_PORTFOLIO_PERMISSION)
    @Description("View your fund portfolio")
    @Syntax("<page>")
    public void onFundPortfolio(Player player, @Optional Integer page) {
        final int safePage = page == null ? 0 : page;
        final UUID uuid = player.getUniqueId();
        final Texts texts = plugin.getTexts();

        plugin.getExecutorService().execute(() -> {
            FundPortfolioResponse response;

            try {
                response = plugin.getFundDataInteractor().getFundPortfolio(uuid, safePage);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.FUND_PORTFOLIO_ERROR, e.getMessage()));
                return;
            }

            Inventory inventory = GuiFactory.createFundPage(ChatColor.GOLD + "Fund Portfolio", response.getFunds(), safePage, response.getPages(), texts);

            Bukkit.getScheduler().runTask(plugin, () -> {
                if (player.isOnline()) {
                    PortfolioViewer portfolioViewer = new PortfolioViewer(player.getUniqueId(), player.getUniqueId(),
                        inventory, PortfolioViewer.InventoryType.FUND_PORTFOLIO, safePage, response.getPages());
                    plugin.getPortfolioTracker().track(player.getUniqueId(), portfolioViewer);
                    player.openInventory(inventory);
                }
            });
        });
    }
}
