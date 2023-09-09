package com.thedasmc.stocks2.commands.funds;

import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.commands.AbstractStocksCommand;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.gui.GuiFactory;
import com.thedasmc.stocks2.gui.PageViewer;
import com.thedasmc.stocks2.requests.request.FundsByCreatorRequest;
import com.thedasmc.stocks2.requests.response.FundResponse;
import com.thedasmc.stocks2.requests.response.FundsByCreatorResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CommandAlias("stocks")
public class FundsCommand extends AbstractStocksCommand {

    private static final String FUNDS_PERMISSION = "stocks.funds";

    public FundsCommand(Stocks2 plugin) {
        super(plugin);
    }

    @Subcommand("funds")
    @CommandPermission(FUNDS_PERMISSION)
    @Description("View funds created by you or another player")
    @Syntax("<player>")
    public void onFundsByCreatorCommand(Player player, @Optional OnlinePlayer otherPlayer, @Default(value = "0") int page) {
        final String creatorName = otherPlayer == null ? player.getName() : otherPlayer.getPlayer().getName();
        final UUID creatorUuid = otherPlayer == null ? player.getUniqueId() : otherPlayer.getPlayer().getUniqueId();
        final Texts texts = plugin.getTexts();

        plugin.getExecutorService().execute(() -> {
            FundsByCreatorRequest request = new FundsByCreatorRequest(creatorUuid, page);
            FundsByCreatorResponse response;

            try {
                response = plugin.getFundDataInteractor().getFundsByCreator(request);
            } catch (IOException e) {
                player.sendMessage(texts.getErrorText(Texts.Types.FUNDS_CREATED_BY_ERROR, e.getMessage()));
                return;
            }

            List<FundResponse> funds = response.getFunds();

            if (otherPlayer != null && !otherPlayer.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                funds = funds.stream()
                    .filter(fundResponse -> fundResponse.getStatus().equals('O'))
                    .collect(Collectors.toList());
            }

            Inventory inventory = GuiFactory.createFundPage(creatorName + "'s Funds", funds, request.getPage(), response.getPages(), texts);

            Bukkit.getScheduler().runTask(plugin, () -> {
                if (player.isOnline()) {
                    PageViewer pageViewer = new PageViewer(player.getUniqueId(), creatorUuid, inventory,
                        PageViewer.InventoryType.FUNDS_CREATED_BY, page, response.getPages());
                    plugin.getPageTracker().track(player.getUniqueId(), pageViewer);
                    player.openInventory(inventory);
                }
            });
        });
    }
}
