package com.account.account.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.account.account.repository.AccountRespository;

import reactor.core.publisher.Mono;

@Service
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    @Autowired
    AccountRespository accountRespository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return accountRespository.findByEmail(username)
                .map(account -> new org.springframework.security.core.userdetails.User(account.getUsername(),
                        account.getPassword(), account.getAuthorities()));
    }

}
