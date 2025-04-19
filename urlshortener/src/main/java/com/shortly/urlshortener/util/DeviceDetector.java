package com.shortly.urlshortener.util;

public class DeviceDetector {
    public static String detectDeviceType(String userAgent) {
        if(userAgent==null) return "Unknown Device";
        userAgent = userAgent.toLowerCase();
        if(userAgent.contains("mobile")) return "Mobile";
        else if(userAgent.contains("tablet")) return "Tablet";
        else if (userAgent.contains("windows") || userAgent.contains("mac") || userAgent.contains("linux")) return "Desktop";
        else return "Unknown";
    }
}
