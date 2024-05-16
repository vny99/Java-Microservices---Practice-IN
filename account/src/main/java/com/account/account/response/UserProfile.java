package com.account.account.response;

public class UserProfile {
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String userName;

    public UserProfile(String email, String firstName, String lastName, String role, String userName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.userName = userName;
    }

    public UserProfile() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "UserProfile [email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", role=" + role
                + ", userName=" + userName + "]";
    }

}
