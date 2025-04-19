package com.shortly.urlshortener.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.shortly.urlshortener.dto.UrlRequest;
import com.shortly.urlshortener.dto.UrlResponse;
import com.shortly.urlshortener.entity.Url;
import com.shortly.urlshortener.repository.UrlRepository;
import com.shortly.urlshortener.util.UrlGenerator;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private final String baseUrl = "http://localhost:8080/";

    public UrlService(UrlRepository urlRepository, RedisTemplate<String, String> redisTemplate) {
        this.urlRepository = urlRepository;
        this.redisTemplate = redisTemplate;
    }

    // 🔐 New method with username
    public UrlResponse generateShortUrl(UrlRequest request, String username) {
        String shortCode = UrlGenerator.generateShortCode();
        LocalDateTime now = LocalDateTime.now();

        Url url = Url.builder()
                .originalUrl(request.getOriginalUrl())
                .shortCode(shortCode)
                .createdAt(now)
                .expirationDate(request.getExpirationDate() != null ? request.getExpirationDate() : now.plusDays(7))
                .userId(username == null ? "no user" : username) // ✅ save username from JWT
                .build();

        urlRepository.save(url);

        long ttl = Duration.between(now, url.getExpirationDate()).toSeconds();
        redisTemplate.opsForValue().set(shortCode, url.getOriginalUrl(), ttl, TimeUnit.SECONDS);

        return new UrlResponse(request.getOriginalUrl(), baseUrl + shortCode);
    }

    public Url getOriginalUrl(String shortCode) {
        String cachedUrl = redisTemplate.opsForValue().get(shortCode);
        if (cachedUrl != null) {
            return Url.builder()
                    .originalUrl(cachedUrl)
                    .shortCode(shortCode)
                    .build();
        }

        return urlRepository.findByShortCode(shortCode)
                .filter(url -> url.getExpirationDate().isAfter(LocalDateTime.now()))
                .map(url -> {
                    long ttl = Duration.between(LocalDateTime.now(), url.getExpirationDate()).getSeconds();
                    redisTemplate.opsForValue().set(shortCode, url.getOriginalUrl(), ttl, TimeUnit.SECONDS);
                    return url;
                })
                .orElse(null);
    }

    // 🔎 List all URLs for a user
    public List<Url> getUrlsByUsername(String username) {
        return urlRepository.findByUserId(username);
    }
}
