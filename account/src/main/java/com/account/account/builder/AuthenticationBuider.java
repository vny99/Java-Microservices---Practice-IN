package com.account.account.builder;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.account.account.response.AuthenticationResponse;

public class AuthenticationBuider {
    private String userName;
    private List<String> authorities;

    public static AuthenticationBuider from(String userName, Collection<? extends GrantedAuthority> collection) {
        return new AuthenticationBuider().withUserName(userName).withAuthorities(collection);
    }

    public AuthenticationBuider withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public AuthenticationBuider withAuthorities(Collection<? extends GrantedAuthority> collection) {
        this.authorities = collection.stream().map(GrantedAuthority::getAuthority).toList();
        return this;
    }

    public AuthenticationResponse build() {
        return new AuthenticationResponse(userName, authorities);
    }
}
