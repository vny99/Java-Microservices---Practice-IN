package com.account.account.requests;



import java.time.LocalDateTime;
import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RegistrationRequest {
    @NotBlank(message = "First Name is required")
	private String firstName;
    @NotBlank(message = "Last Name is required")
	private String lastName;
    @NotBlank(message = "Email is required")
	private String email;
    @NotNull(message = "dob is required")
	private LocalDateTime  dob;
    @NotBlank(message = "Password is required")
	private String password;
    @NotBlank(message = "Role is required")
	private String role;
    public RegistrationRequest() {
    }
    public RegistrationRequest(@NotBlank(message = "First Name is required") String firstName,
            @NotBlank(message = "Last Name is required") String lastName,
            @NotBlank(message = "Email is required") String email, @NotBlank(message = "dob is required") LocalDateTime dob,
            @NotBlank(message = "Password is required") String password,
            @NotBlank(message = "Role is required") String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dob = dob;
        this.password = password;
        this.role = role;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDateTime getDob() {
        return dob;
    }
    public void setDob(LocalDateTime dob) {
        this.dob = dob;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
   
    @Override
    public String toString() {
        return "RegistrationRequest [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", dob="
                + dob + ", password=" + password + ", role=" + role + "]";
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    
}
