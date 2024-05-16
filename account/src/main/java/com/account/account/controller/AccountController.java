package com.account.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.account.account.builder.RespBuilder;
import com.account.account.config.JWTService;
import com.account.account.requests.RegistrationRequest;
import com.account.account.response.AuthenticationResponse;
import com.account.account.response.Resp;
import com.account.account.service.AccountService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JWTService jwtService;

    private static final String ACCOUNT_DELETED_SUCCESSFULL = "Account deletetion Successfull : ";

    private static final String ACCOUNT_UPDATAION_SUCCESSFULL = "Account updatation Successfull : ";

    private static final String AUTH_HEADER = "X-Authorities";
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private static final String ACCESS_DENIED = "ONLY ADMIN CAN PERFORM THIS OPERATION";

    @GetMapping("/get")
    public Mono<ResponseEntity<Resp>> getAccount(@RequestParam String email,
            @RequestHeader(AUTH_HEADER) String authorities) {
        if (!authorities.contains(ADMIN) && !authorities.contains(USER)) {
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(RespBuilder.from(ACCESS_DENIED, HttpStatus.FORBIDDEN).build()));
        }
        return accountService.getUserProfile(email)
                .map(account -> ResponseEntity.ok().body(RespBuilder.from(account, HttpStatus.OK).build()));
    }

    @GetMapping("/getAll")
    public Mono<ResponseEntity<Resp>> getAllAccounts(@RequestHeader(AUTH_HEADER) String authorities) {
        if (!authorities.contains(ADMIN)) {
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(RespBuilder.from(ACCESS_DENIED, HttpStatus.FORBIDDEN).build()));
        }
        return accountService.getAllAccounts().collectList()
                .map(accounts -> ResponseEntity.ok().body(RespBuilder.from(accounts, HttpStatus.OK).build()));
    }

    @DeleteMapping("/delete")
    public Mono<ResponseEntity<Resp>> deleteAccount(@RequestParam String email,
            @RequestHeader(AUTH_HEADER) String authorities) {
        if (!authorities.contains(ADMIN)) {
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(RespBuilder.from(ACCESS_DENIED, HttpStatus.FORBIDDEN).build()));
        }
        return accountService.deleteAccount(email).then(Mono.just(ResponseEntity.ok()
                .body(RespBuilder.from(ACCOUNT_DELETED_SUCCESSFULL + email, HttpStatus.OK).build())));
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<Resp>> updateAccount(@Valid @RequestBody RegistrationRequest request,
            @RequestParam String email, @RequestHeader(AUTH_HEADER) String authorities) {
        if (!authorities.contains(ADMIN)) {
            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(RespBuilder.from(ACCESS_DENIED, HttpStatus.FORBIDDEN).build()));
        }
        return accountService.updateAccount(email, request).then(Mono.just(ResponseEntity.ok()
                .body(RespBuilder.from(ACCOUNT_UPDATAION_SUCCESSFULL + email, HttpStatus.OK).build())))
                .onErrorResume(ex -> Mono.just(ResponseEntity.badRequest()
                        .body(RespBuilder.from(ex.getMessage(), HttpStatus.BAD_REQUEST).build())));
    }

    @GetMapping("/getUserName")
    public Mono<String> getUserName() {
        return accountService.getUserName();
    }

    @GetMapping("/getSecurityContext")
    public Mono<AuthenticationResponse> getSecurityContext(@RequestParam String token) {
        return jwtService.getSecurityContext(token);
    }

    @GetMapping("/isTokenValid")
    public Mono<Boolean> isTokenValid(@RequestParam String token) {
        return Mono.just(jwtService.isTokenValid(token));
    }

}
