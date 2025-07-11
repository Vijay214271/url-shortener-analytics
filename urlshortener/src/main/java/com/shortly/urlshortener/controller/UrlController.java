package com.shortly.urlshortener.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shortly.urlshortener.dto.UrlRequest;
import com.shortly.urlshortener.dto.UrlResponse;
import com.shortly.urlshortener.entity.ClickAnalytics;
import com.shortly.urlshortener.entity.Url;
import com.shortly.urlshortener.repository.ClickAnalyticsRepository;
import com.shortly.urlshortener.service.UrlService;
import com.shortly.urlshortener.util.GeoLocationUtil;
import com.shortly.urlshortener.util.UserAgentsUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "https://url-shortener-analytics.vercel.app/")
public class UrlController {

    private final UrlService urlService;
    private final ClickAnalyticsRepository clickAnalyticsRepository;
    @Autowired
    private final GeoLocationUtil geoLocationUtil;

    public UrlController(UrlService urlService, ClickAnalyticsRepository clickAnalyticsRepository) {
        this.urlService = urlService;
        this.clickAnalyticsRepository = clickAnalyticsRepository;
        this.geoLocationUtil = null;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<UrlResponse> createShortLink(@RequestBody UrlRequest request, HttpServletRequest req) {
    String username = (String) req.getAttribute("username"); // set in JwtFilter
    System.out.println("🔐 Username received in controller: " + username);
    UrlResponse response = urlService.generateShortUrl(request, username);
    return ResponseEntity.ok(response);
    }

    // ✅ Redirect and save click analytics (public)
    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response, HttpServletRequest request) throws IOException {
        Url url = urlService.getOriginalUrl(shortCode);

        if (url != null) {
            String ip = request.getRemoteAddr();
            String fullUserAgent = request.getHeader("User-Agent");
            String userAgent = UserAgentsUtil.shortenUserAgent(fullUserAgent);
            String deviceType = userAgent != null && userAgent.toLowerCase().contains("mobile") ? "mobile" : "Desktop";
            LocalDateTime time = LocalDateTime.now();

            String[] location = geoLocationUtil.getLocationFromIP(ip);
            String city = location[0];
            String country = location[1]; 

            System.out.println("Analytics Data:");
            System.out.println("IP: " + ip);
            System.out.println("User-Agent: " + userAgent);
            System.out.println("Device: " + deviceType);
            System.out.println("Country: " + country);
            System.out.println("City: " + city);
            System.out.println("Time: " + time);

            ClickAnalytics clickAnalytics = ClickAnalytics.builder()
                    .shortCode(shortCode)
                    .ipAddress(ip)
                    .userAgent(userAgent)
                    .deviceType(deviceType)
                    .timestamp(LocalDateTime.now())
                    .city(city)
                    .country(country)
                    .build();

            clickAnalyticsRepository.save(clickAnalytics);

            response.sendRedirect(url.getOriginalUrl());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // 🔐 Get only the logged-in user’s URLs
    @GetMapping("/api/urls")
    public ResponseEntity<List<Url>> getUserUrls(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        System.out.println("🧩 Retrieved username in controller: " + username);
        List<Url> urls = urlService.getUrlsByUsername(username);
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/api/analytics/export/{shortCode}")
    public void exportAnalyticsCSV(@PathVariable String shortCode, HttpServletResponse response, HttpServletRequest request) throws IOException {
        List<ClickAnalytics> analytics = clickAnalyticsRepository.findAll().stream().filter(c->c.getShortCode().equals(shortCode)).collect(Collectors.toList());

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachmment; filename:\""+shortCode+"-analytics.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("IP Address,Device Type,User Agent,City,Country,Timestamp");
            
            for(ClickAnalytics c: analytics) {
                writer.printf("%s,%s,%s,%s,%s,%s%n",
                        c.getIpAddress(),
                        c.getDeviceType(),
                        c.getUserAgent().replace(",", " "),
                        c.getCity(),
                        c.getCountry(),
                        c.getTimestamp()
                );
            }
            
            writer.flush();
        }
    }

    @GetMapping("/api/analytics/daily/{shortCode}")
    public ResponseEntity<Map<String, Long>> getDailyClickStats(@PathVariable String shortCode) {
        List<ClickAnalytics> analytics = clickAnalyticsRepository.findAll().stream().filter(a->a.getShortCode().equals(shortCode)).collect(Collectors.toList());
        
        Map<String, Long> dailyStats = analytics.stream().collect(Collectors.groupingBy(
        a->a.getTimestamp().toLocalDate().toString(),
        Collectors.counting()
        ));

        return ResponseEntity.ok(dailyStats);
    }

    // Return full list of click analytics for a short code
    @GetMapping("/api/analytics/{shortCode}")
    public ResponseEntity<List<ClickAnalytics>> getClickAnalytics(@PathVariable String shortCode) {
        List<ClickAnalytics> analytics = clickAnalyticsRepository.findAll()
            .stream()
            .filter(c -> c.getShortCode().equals(shortCode))
            .collect(Collectors.toList());

        return ResponseEntity.ok(analytics);
    }

    @DeleteMapping("/api/urls/{shortCode}")
    public ResponseEntity<String> deleteUrl(@PathVariable String shortCode,HttpServletRequest request){
        String username = (String) request.getAttribute("username");
        System.out.println("🔒 Authenticated user trying to delete: " + username); 
        boolean deleted = urlService.deleteUrlByShortCode(shortCode, username);
        if(deleted) {
            return ResponseEntity.ok("✅ URL deleted successfully.");
        }
        else {
            return ResponseEntity.status(403).body("❌ Unauthorized or URL not found.");
        }
    }

    }