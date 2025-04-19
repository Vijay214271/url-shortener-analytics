package com.shortly.urlshortener.util;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GeoLocationUtil {

    private static final String API_URL = "https://ipapi.co/";

    private final RestTemplate restTemplate;

    public GeoLocationUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String[] getLocationFromIP(String ip) {
        try {
            String response = restTemplate.getForObject(API_URL + ip + "/json/", String.class);
            if (response == null) {
                return new String[]{"Unknown", "Unknown"};
            }

            JSONObject json = new JSONObject(response);
            String city = json.optString("city", "unknown");
            String country = json.optString("country_name", "unknown");

            return new String[]{city, country};
        } catch (Exception e) {
            // Log the error (you can log it using your preferred logger like SLF4J)
            e.printStackTrace();
            return new String[]{"Unknown", "Unknown"};
        }
    }
}
