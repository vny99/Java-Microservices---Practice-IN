package com.account.account.builder;

import com.account.account.entity.Account;
import com.account.account.entity.Role;
import com.account.account.response.UserProfile;

public class UserProfileBuilder {
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String userName;
    
    public static UserProfileBuilder from (Account account){
        return new UserProfileBuilder().withEmail(account.getEmail()).withFirstName(account.getFirstName()).withLastName(account.getLastName()).withRole(account.getRole()).withUserName(account.getUsername());
    }

    private UserProfileBuilder withEmail(String email) {
       this.email = email;
       return this;
    }

    private UserProfileBuilder withFirstName(String firstName){
        this.firstName = firstName;
        return this;
    }

    private UserProfileBuilder withLastName(String lastName){
        this.lastName = lastName;
        return this;
    }

    private UserProfileBuilder withRole(Role role){
        this.role = role.name();
        return this;
    }

    private UserProfileBuilder withUserName(String userName){
        this.userName = userName;
        return this;
    }

    public UserProfile build(){
        return new UserProfile(email, firstName, lastName, role, userName);
    }

}
