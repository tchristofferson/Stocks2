package com.thedasmc.stocks2.requests.request;

public class ResetAccountPasswordEmailRequest {

    private String email;

    public ResetAccountPasswordEmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
