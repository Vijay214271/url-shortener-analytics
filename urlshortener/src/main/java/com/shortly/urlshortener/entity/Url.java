package com.shortly.urlshortener.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "urls")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Url {
    @Id
    private String id;
    
    private String originalUrl;
    private String shortCode;
    private LocalDateTime createdAt;
    private LocalDateTime expirationDate;

    private String userId;
}
