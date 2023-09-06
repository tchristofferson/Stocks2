package com.thedasmc.stocks2.requests.interactors.impl;

import com.google.gson.Gson;
import com.thedasmc.stocks2.common.Constants;
import com.thedasmc.stocks2.common.Tools;
import com.thedasmc.stocks2.requests.interactors.AbstractAccountInteractor;
import com.thedasmc.stocks2.requests.request.AccountRequest;
import com.thedasmc.stocks2.requests.request.ResetAccountPasswordEmailRequest;
import com.thedasmc.stocks2.requests.response.AccountRegistrationResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.thedasmc.stocks2.common.Tools.*;

public class AccountInteractor extends AbstractAccountInteractor {

    private static final String REGISTER_URI = "/v1/account/register";
    private static final String REQUEST_ACCOUNT_PASS_RESET_URI = "/v1/account/reset/send";

    public AccountInteractor(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    @Override
    public AccountRegistrationResponse registerAccount(AccountRequest request) throws IOException {
        URL url = new URL(Constants.API_URL + REGISTER_URI);
        HttpURLConnection connection = Tools.getHttpPostConnection(url, null);
        String requestJson = this.gson.toJson(request);
        writeBody(connection, requestJson);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            throw new IOException(readErrorStream(connection.getErrorStream()));
        }

        return this.gson.fromJson(responseJson, AccountRegistrationResponse.class);
    }

    @Override
    public void requestAccountPasswordReset(String email) throws IOException {
        URL url = new URL(Constants.API_URL + REQUEST_ACCOUNT_PASS_RESET_URI);
        HttpURLConnection connection = Tools.getHttpPostConnection(url, this.apiToken);
        ResetAccountPasswordEmailRequest request = new ResetAccountPasswordEmailRequest(email);
        String requestJson = this.gson.toJson(request);
        writeBody(connection, requestJson);

        try {
            connection.getResponseCode();
        } catch (IOException e) {
            throw new IOException(readErrorStream(connection.getErrorStream()));
        }
    }
}
