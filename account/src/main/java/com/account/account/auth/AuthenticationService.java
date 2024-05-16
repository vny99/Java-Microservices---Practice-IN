package com.account.account.auth;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.account.account.builder.AccountBuilder;
import com.account.account.config.JWTService;
import com.account.account.dao.AccountDAO;
import com.account.account.requests.AuthenticationRequest;
import com.account.account.requests.RegistrationRequest;

import reactor.core.publisher.Mono;

@Service
public class AuthenticationService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ReactiveAuthenticationManager reactiveAuthenticationManager;

    @Autowired
    private AccountDAO accountDAO;

    private static final String TOKEN = "token";

    private final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    public Mono<Void> register(RegistrationRequest request) {
        return accountDAO.saveAccount(AccountBuilder.from(request, encoder).build());
    }

    public Mono<Void> authenticate(AuthenticationRequest request, ServerHttpResponse response) {
        return reactiveAuthenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()))
                .then(accountDAO.getAccount(request.getEmail())).flatMap(account -> {
                    String token = jwtService.generateToken(account);
                    response.addCookie(ResponseCookie.from(TOKEN, token).httpOnly(true).path("/")
                            .maxAge(Duration.ofSeconds(3600)).build());
                    return Mono.empty();
                });
    }

    public Mono<Void> logout(ServerHttpResponse response) {
        return Mono.justOrEmpty(response).flatMap(serverResponse -> {
            serverResponse.addCookie(ResponseCookie.from(TOKEN, null).httpOnly(true).path("/")
                    .maxAge(Duration.ofSeconds(3600)).build());
            return Mono.empty();
        });
    }


}
