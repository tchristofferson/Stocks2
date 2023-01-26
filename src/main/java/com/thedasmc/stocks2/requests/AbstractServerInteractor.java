package com.thedasmc.stocks2.requests;

import com.google.gson.Gson;
import com.thedasmc.stocks2.requests.request.AccountRequest;
import com.thedasmc.stocks2.requests.response.ServerRegistrationResponse;

import java.io.IOException;

public abstract class AbstractServerInteractor extends DataInteractor {

    public AbstractServerInteractor(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    /**
     * Register a server for the specified account
     * @param request The {@link AccountRequest} containing the email and password for the server to be registered under
     * @return A {@link ServerRegistrationResponse}
     * @throws IOException If an error occurs
     */
    public abstract ServerRegistrationResponse registerServer(AccountRequest request) throws IOException;

}
