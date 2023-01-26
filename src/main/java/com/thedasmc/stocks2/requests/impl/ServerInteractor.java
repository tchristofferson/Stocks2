package com.thedasmc.stocks2.requests.impl;

import com.google.gson.Gson;
import com.thedasmc.stocks2.common.Constants;
import com.thedasmc.stocks2.common.Tools;
import com.thedasmc.stocks2.requests.AbstractServerInteractor;
import com.thedasmc.stocks2.requests.request.AccountRequest;
import com.thedasmc.stocks2.requests.response.ServerRegistrationResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.thedasmc.stocks2.common.Tools.*;

public class ServerInteractor extends AbstractServerInteractor {

    private static final String REGISTER_URI = "/v1/server/register";

    public ServerInteractor(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    @Override
    public ServerRegistrationResponse registerServer(AccountRequest request) throws IOException {
        URL url = new URL(Constants.API_URL + REGISTER_URI);
        HttpURLConnection connection = Tools.getHttpPostConnection(url);
        String requestJson = this.gson.toJson(request);
        writeBody(connection, requestJson);
        String responseJson;

        try {
            responseJson = readInputStream(connection.getInputStream());
        } catch (IOException e) {
            if (connection.getResponseCode() == 401)//Checking for 401 unauthorized because it will cause errorStream to be null
                throw new IOException("Invalid credentials!");

            throw new IOException(readErrorStream(connection.getErrorStream()));
        }

        return this.gson.fromJson(responseJson, ServerRegistrationResponse.class);
    }
}
