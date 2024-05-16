package com.account.account.builder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.account.account.entity.Account;
import com.account.account.entity.Role;
import com.account.account.requests.RegistrationRequest;

public class AccountBuilder {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
    private LocalDateTime dob;

    public static AccountBuilder from(RegistrationRequest request, PasswordEncoder encoder) {
        return new AccountBuilder().withFirstName(request.getFirstName())
                .withLastName(request.getLastName())
                .withEmail(request.getEmail())
                .withPassword(encoder.encode(request.getPassword()))
                .withRole(request.getRole())
                .withDob(request.getDob());
    }

    private AccountBuilder withDob(LocalDateTime dob) {
        this.dob = dob;
        return this;
    }

    private AccountBuilder withRole(String role) {
        this.role = Role.parse(role);
        return this;
    }

    private AccountBuilder withPassword(String encode) {
        this.password = encode;
        return this;
    }

    private AccountBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    private AccountBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    private AccountBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Account build() {
        return new Account(UUID.randomUUID().toString(), firstName, lastName, email, password, dob, role);
    }
}
