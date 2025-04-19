package com.shortly.urlshortener.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "clicks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClickAnalytics {
    @Id
    private String id;

    private String shortCode;
    private String ipAddress;
    private String userAgent;
    private String deviceType;
    private LocalDateTime timestamp;
    private String country;
    private String city;

}
