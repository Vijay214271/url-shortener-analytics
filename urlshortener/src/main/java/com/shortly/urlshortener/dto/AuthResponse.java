package com.shortly.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO for response
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
}

