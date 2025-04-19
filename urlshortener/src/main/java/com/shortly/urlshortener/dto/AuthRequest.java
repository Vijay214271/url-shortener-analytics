package com.shortly.urlshortener.dto;

import lombok.Data;

// DTO for login/signup
@Data
public class AuthRequest {
    private String username;
    private String password;
}

