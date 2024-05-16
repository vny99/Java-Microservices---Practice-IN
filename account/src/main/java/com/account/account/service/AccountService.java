package com.account.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.account.account.dao.AccountDAO;
import com.account.account.entity.Account;
import com.account.account.requests.RegistrationRequest;
import com.account.account.response.UserProfile;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountService {

    @Autowired
    private AccountDAO accountDAO;



    public Mono<Account> getAccount(String email) {
        return accountDAO.getAccount(email);
    }

    public Mono<Void> updateAccount(String email, RegistrationRequest request) {
        return accountDAO.updateAccount(email, request);
    }

    public Mono<Void> deleteAccount(String email) {
        return accountDAO.deleteAccount(email);
    }

    public Mono<UserProfile> getUserProfile(String email) {
        return accountDAO.getUserProfile(email);
    }

    public Flux<UserProfile> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public Mono<String> getUserName() {
        
        return ReactiveSecurityContextHolder.getContext().map(securityContext -> {
           return (User) securityContext.getAuthentication().getPrincipal();
        }).map(User::getUsername);
    }

   

}
