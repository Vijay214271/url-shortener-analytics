package com.shortly.urlshortener.util;

import java.util.UUID;

public class UrlGenerator {
    public static String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}