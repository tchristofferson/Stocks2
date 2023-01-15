package com.thedasmc.stocks2.requests.request;

public class AccountRequest {

    private final String email;
    private final String password;

    public AccountRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
