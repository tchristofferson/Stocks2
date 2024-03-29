package com.thedasmc.stocks2.requests.interactors;

import com.google.gson.Gson;
import com.thedasmc.stocks2.requests.request.AccountRequest;
import com.thedasmc.stocks2.requests.response.AccountRegistrationResponse;

import java.io.IOException;

public abstract class AbstractAccountInteractor extends DataInteractor {

    public AbstractAccountInteractor(String apiToken, Gson gson) {
        super(apiToken, gson);
    }

    /**
     * Register an account
     * @param request The {@link AccountRequest} containing the email and password to be registered
     * @return An {@link AccountRegistrationResponse}
     * @throws IOException If an error occurs
     */
    public abstract AccountRegistrationResponse registerAccount(AccountRequest request) throws IOException;

    /**
     * Request email to be sent to reset account password
     * @param email The email for the account to reset the password for
     * @throws IOException If an error occurs
     */
    public abstract void requestAccountPasswordReset(String email) throws IOException;

}
