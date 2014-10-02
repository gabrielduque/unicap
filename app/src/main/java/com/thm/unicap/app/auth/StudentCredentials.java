package com.thm.unicap.app.auth;

public class StudentCredentials {
    private String registration;
    private String password;
    private String authToken;

    public StudentCredentials(String registration, String password) {
        this.registration = registration;
        this.password = password;
    }

    public StudentCredentials(String registration, String password, String authToken) {
        this.registration = registration;
        this.password = password;
        this.authToken = authToken;
    }

    public StudentCredentials() {

    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
