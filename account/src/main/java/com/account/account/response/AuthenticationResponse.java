package com.account.account.response;

import java.util.List;

public class AuthenticationResponse {

    private String userName;
    private List<String> authorities;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String userName, List<String> authorities2) {
        this.userName = userName;
        this.authorities = authorities2;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "AuthenticationResponse [userName=" + userName + ", authorities=" + authorities + "]";
    }

}
