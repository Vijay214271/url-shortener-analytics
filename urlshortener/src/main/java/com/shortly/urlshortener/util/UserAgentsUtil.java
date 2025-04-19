package com.shortly.urlshortener.util;

public class UserAgentsUtil {
    public static String shortenUserAgent(String userAgent) {
        if (userAgent == null) return "Unknown";

        // Detect Browser
        String browser = userAgent.contains("Chrome") ? "Chrome" :
                         userAgent.contains("Firefox") ? "Firefox" :
                         userAgent.contains("Safari") ? "Safari" :
                         userAgent.contains("Edge") ? "Edge" :
                         "Other";

        // Detect OS
        String os = userAgent.contains("Windows") ? "Windows" :
                    userAgent.contains("Mac") ? "Mac" :
                    userAgent.contains("Linux") ? "Linux" :
                    userAgent.contains("Android") ? "Android" :
                    userAgent.contains("iPhone") ? "iOS" :
                    "Other";

        return os + " - " + browser;
    }
}
