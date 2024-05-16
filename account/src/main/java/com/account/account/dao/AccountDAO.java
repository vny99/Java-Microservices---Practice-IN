package com.account.account.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.account.account.builder.AccountBuilder;
import com.account.account.builder.UserProfileBuilder;
import com.account.account.entity.Account;
import com.account.account.exceptions.UserAlreadyExistsException;
import com.account.account.repository.AccountRespository;
import com.account.account.requests.RegistrationRequest;
import com.account.account.response.UserProfile;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountDAO {

    @Autowired
    private AccountRespository accountRepository;

    @Autowired
    private PasswordEncoder encoder;

    private final Logger LOG = LoggerFactory.getLogger(AccountDAO.class);

    private static final String USER_NOT_FOUND = "User not found :";
    private static final String USER_ALREADY_EXISTS = "User already exists :";

    public Mono<Account> getAccount(String email) {
        return accountRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(USER_NOT_FOUND)));
    }

    public Mono<Void> saveAccount(Account account) {
        return accountRepository.findByEmail(account.getUsername())
                .flatMap(existingAccount -> Mono
                        .error(new UserAlreadyExistsException(USER_ALREADY_EXISTS + account.getEmail())))
                .switchIfEmpty(accountRepository.save(account)).then();
    }

    public Mono<Void> updateAccount(String email, RegistrationRequest request) {
        Account account = AccountBuilder.from(request, encoder).build();
        return getAccount(email).flatMap(oldAccount -> {
            return accountRepository.updateAccount(oldAccount.getId(), account.getFirstName(), account.getLastName(),
                    account.getEmail(), account.getPassword(), account.getDob(), account.getRole()).then();
        });
    }

    public Mono<Void> deleteAccount(String email) {
        return accountRepository.existsByEmail(email).flatMap(exists -> {
            if (!exists) {
                return Mono.error(new UsernameNotFoundException(USER_NOT_FOUND + email));
            } else {
                return accountRepository.deleteByEmail(email);
            }
        });
    }

    public Mono<UserProfile> getUserProfile(String email) {
        return accountRepository.findByEmail(email)
                .flatMap(account -> Mono.just(UserProfileBuilder.from(account).build()))
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(USER_NOT_FOUND)));
    }

    public Flux<UserProfile> getAllAccounts() {
        return accountRepository.findAll().map(UserProfileBuilder::from).map(UserProfileBuilder::build);
    }

    public Mono<Boolean> existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

}
