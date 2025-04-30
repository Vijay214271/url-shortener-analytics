package com.shortly.urlshortener.dto;

public class AuthResponse {
    private String token;

    public AuthResponse() {} // REQUIRED

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() { // REQUIRED
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
