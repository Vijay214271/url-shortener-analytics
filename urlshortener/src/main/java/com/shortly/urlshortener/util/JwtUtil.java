package com.shortly.urlshortener.util;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {
    private final String secretKey = Base64.getEncoder().encodeToString("VIJAYAKUMARPATNALA1234567890abc7899".getBytes());
    private final int jwtExpirationInMs = 604800000;
    // public JwtUtil() throws NoSuchAlgorithmException {
    //     KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
    //     SecretKey sk = keyGen.generateKey();
    //     secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
    // }

    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    public String generateToken(Map<String, Object> extraClaims, String username ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(getKey())
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isValid(String token) {
        try {
            // Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
     

    private boolean isTokenExpired(String token) {
        // return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
        Date expiration = Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }
}

