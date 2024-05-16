package com.account.account.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.account.builder.RespBuilder;
import com.account.account.config.JWTService;
import com.account.account.requests.AuthenticationRequest;
import com.account.account.requests.RegistrationRequest;
import com.account.account.response.Resp;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JWTService jwtService;

    private static final String SUCCESSFULL_REGISTRATION = "User registered successfully";
    private static final String SUCCESSFULL_LOGIN = "User logged in successfully";
    private static final String SUCCESSFULL_LOGOUT = "User logged out successfully";

    @PostMapping("/register")
    public Mono<ResponseEntity<Resp>> registerUser(@Valid @RequestBody RegistrationRequest request) {
        return authenticationService.register(request).then(
                Mono.just(ResponseEntity.ok().body(RespBuilder.from(SUCCESSFULL_REGISTRATION, HttpStatus.OK).build())))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest()
                        .body(RespBuilder.from(e.getMessage(), HttpStatus.BAD_REQUEST).build())));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Resp>> login(@Valid @RequestBody AuthenticationRequest request,
            ServerHttpResponse serverHttpResponse) {
        return authenticationService.authenticate(request, serverHttpResponse)
                .then(Mono.just(ResponseEntity.ok().body(RespBuilder.from(SUCCESSFULL_LOGIN, HttpStatus.OK).build())))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest()
                        .body(RespBuilder.from(e.getMessage(), HttpStatus.BAD_REQUEST).build())));
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Resp>> logout(ServerHttpResponse httpResponse) {
        return authenticationService.logout(httpResponse)
                .then(Mono.just(ResponseEntity.ok().body(RespBuilder.from(SUCCESSFULL_LOGOUT, HttpStatus.OK).build())));
    }

}
