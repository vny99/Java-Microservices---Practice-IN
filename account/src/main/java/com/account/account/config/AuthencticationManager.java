package com.account.account.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import reactor.core.publisher.Mono;

@Configuration
public class AuthencticationManager implements ReactiveAuthenticationManager {

    @Autowired
    private ReactiveUserDetailsService reactiveUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        return reactiveUserDetailsService.findByUsername(username).flatMap(account -> {
            if (passwordEncoder.matches(password, account.getPassword())) {
                return Mono.just(new UsernamePasswordAuthenticationToken(username, account.getPassword(),
                        account.getAuthorities()));
            } else {
                return Mono.error(new BadCredentialsException("Bad credentials for :" + username));
            }
        });
    }

}
