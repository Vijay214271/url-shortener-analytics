package com.shortly.urlshortener.util;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

public class GeoLocationUtil {
    private static final String API_URL = "https://ipapi.co/";

    public static String[] getLocationFromIP(String ip) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(API_URL+ip+"/json/",String.class);
            JSONObject json = new JSONObject(response);

            String city = json.optString("city","unknown");
            String country = json.optString("country_name", "unknown");

            return new String[]{city,country};
        }
        catch (Exception e) {
            return new String[]{"Unknown", "Unknown"};
        }
    }
}
