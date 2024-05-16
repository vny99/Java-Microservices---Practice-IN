package com.account.account.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.management.relation.Role;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.account.account.entity.Account;

import reactor.core.publisher.Mono;
@Repository
public interface AccountRespository extends R2dbcRepository<Account, String>{

    Mono<Account> findByEmail(String username);

    Mono<Boolean> existsByEmail(String email);

    Mono<Void> deleteByEmail(String email);

    @Modifying
    @Query("UPDATE account SET firstName = :firstName, lastName = :lastName, email = :email, password = :password, dob = :dob, role = :role WHERE id = :id")
    Mono<Integer> updateAccount(String id, String firstName, String lastName, String email, String password, LocalDateTime dob, com.account.account.entity.Role role);
    
}
