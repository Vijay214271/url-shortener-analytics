package com.shortly.urlshortener.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.shortly.urlshortener.util.JwtUtil;

import org.springframework.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    public static final String USERNAME_ATTR = "username";

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
            throws ServletException, IOException {
                String authHeader = request.getHeader("Authorization");

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    if (jwtUtil.isValid(token)) {
                        String username = jwtUtil.extractUsername(token);
                        request.setAttribute(USERNAME_ATTR, username);
                        System.out.println("✅ Username set in request: " + username);
                    } else {
                        System.out.println("❌ Invalid token");
                    }
                } else {
                    System.out.println("⚠️ No Authorization header found");
                }
        
                filterChain.doFilter(request, response);
    }
    
}
