package com.thedasmc.stocks2.commands;

import co.aikar.commands.BaseCommand;
import com.thedasmc.stocks2.Stocks2;
import com.thedasmc.stocks2.common.Texts;
import com.thedasmc.stocks2.requests.interactors.AbstractFundDataInteractor;
import com.thedasmc.stocks2.requests.request.FundTransactionRequest;
import com.thedasmc.stocks2.requests.response.FundResponse;
import com.thedasmc.stocks2.requests.response.FundValueResponse;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public abstract class AbstractStocksCommand extends BaseCommand {

    protected final Stocks2 plugin;

    protected AbstractStocksCommand(Stocks2 plugin) {
        this.plugin = plugin;
    }

    /**
     * Fetch a fund. Will perform web request. Must be called async
     * @param sender The sender getting the fund
     * @param fundId The fund's id
     * @return A {@link FundResponse} representing the fund, {@code null} if an error occurred
     */
    protected FundResponse getFund(CommandSender sender, long fundId) {
        AbstractFundDataInteractor fundDataInteractor = plugin.getFundDataInteractor();
        Texts texts = plugin.getTexts();

        FundResponse fundResponse;

        try {
            fundResponse = fundDataInteractor.getFund(fundId);
        } catch (IOException e) {
            sender.sendMessage(texts.getErrorText(Texts.Types.FUND_FETCH_ERROR, e.getMessage()));
            return null;
        }

        return fundResponse;
    }

    /**
     * Checks to see if the player is the fund creator.<br>
     * If the player isn't the creator, an error message is sent to the player
     * @param sender The command sender running the command
     * @param uuidToCheck The uuid to check if they are the fund's creator
     * @param fundResponse The fund to check if the player is the creator
     * @return {@code false} if the player is the creator, otherwise {@code true}
     */
    protected boolean isNotFundCreator(CommandSender sender, UUID uuidToCheck, FundResponse fundResponse) {
        if (!fundResponse.getCreatorId().equals(uuidToCheck)) {
            sender.sendMessage(plugin.getTexts().getText(Texts.Types.NOT_FUND_CREATOR));
            return true;
        }

        return false;
    }

    /**
     * Update a fund's status. Will perform web request. Must be called async
     * @param sender The command sender running the command
     * @param updaterUuid The uuid of the player updating the fund status
     * @param fundId The fundId of the fund to be updated
     * @param status The status to update the fund with
     * @return {@code true} if successful, otherwise {@code false}
     */
    protected boolean updateFundStatus(CommandSender sender, UUID updaterUuid, long fundId, char status) {
        FundResponse fundResponse = getFund(sender, fundId);

        if (fundResponse == null)
            return false;

        if (isNotFundCreator(sender, updaterUuid, fundResponse))
            return false;

        try {
            plugin.getFundDataInteractor().updateFundStatus(fundId, status);
        } catch (IOException e) {
            sender.sendMessage(plugin.getTexts().getErrorText(Texts.Types.FUND_STATUS_UPDATE_ERROR, e.getMessage()));
            return false;
        }

        return true;
    }

    /**
     * Deposit/Withdraw money from a fund. Will perform web request. Must be called async
     * @param sender The command sender running the command
     * @param transactorUuid The uuid of the player depositing/withdrawing
     * @param fundId The fundId of the fund the player is depositing/withdrawing
     * @param amount The amount to deposit/withdraw
     * @return {@code true} if successful, otherwise {@code false}
     */
    protected boolean transactFund(CommandSender sender, UUID transactorUuid, long fundId, double amount) {
        AbstractFundDataInteractor fundDataInteractor = plugin.getFundDataInteractor();
        long centsAmount = (long) (amount * 100);
        //Should this transaction deposit into vault balance?
        //If this is true a player is withdrawing from fund, which is deposited into their vault balance
        boolean isEconDeposit = centsAmount < 0;
        FundValueResponse fundValueResponse;

        try {
            fundValueResponse = fundDataInteractor.getPlayerFundInvestmentValue(fundId, transactorUuid);
        } catch (IOException e) {
            sender.sendMessage(plugin.getTexts().getErrorText(Texts.Types.TRANSACT_FUND_ERROR, e.getMessage()));
            return false;
        }

        if (isEconDeposit && fundValueResponse.getCentsValue().compareTo(BigInteger.valueOf(Math.abs(centsAmount))) < 0) {
            sender.sendMessage(plugin.getTexts().getText(Texts.Types.NOT_ENOUGH_SHARES));
            return false;
        }

        Future<EconomyResponse> economyResponseFuture = Bukkit.getScheduler().callSyncMethod(plugin, () -> {
            Economy economy = plugin.getEconomy();
            EconomyResponse economyResponse;
            OfflinePlayer player = Bukkit.getOfflinePlayer(transactorUuid);

            if (isEconDeposit) {
                economyResponse = economy.depositPlayer(player, Math.abs(centsAmount / 100D));
            } else {
                economyResponse = economy.withdrawPlayer(player, Math.abs(centsAmount / 100D));
            }

            return economyResponse;
        });

        EconomyResponse economyResponse;

        try {
            economyResponse = economyResponseFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            sender.sendMessage(plugin.getTexts().getErrorText(Texts.Types.TRANSACT_FUND_ERROR, e.getMessage()));
            return false;
        }

        if (!economyResponse.transactionSuccess()) {
            sender.sendMessage(plugin.getTexts().getErrorText(Texts.Types.TRANSACT_FUND_ERROR, economyResponse.errorMessage));
            return false;
        }

        FundTransactionRequest request = new FundTransactionRequest();
        request.setFundId(fundId);
        request.setPlayerId(transactorUuid);
        request.setCents(centsAmount);

        try {
            fundDataInteractor.transactFund(request);
        } catch (IOException e) {
            //Call failed, set player balance back to what it was
            Bukkit.getScheduler().callSyncMethod(plugin, () -> {
                OfflinePlayer player = Bukkit.getOfflinePlayer(transactorUuid);
                Economy economy = plugin.getEconomy();
                EconomyResponse undoEconResponse;

                if (isEconDeposit) {
                    undoEconResponse = economy.withdrawPlayer(player, Math.abs(centsAmount / 100D));
                } else {
                    undoEconResponse = economy.depositPlayer(player, Math.abs(centsAmount / 100D));
                }

                return undoEconResponse;
            });

            sender.sendMessage(plugin.getTexts().getErrorText(Texts.Types.TRANSACT_FUND_ERROR, e.getMessage()));
            return false;
        }

        return true;
    }
}
