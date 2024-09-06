package org.example.exercise_shop.utils;

import java.util.Base64;

public class CookieUtils {
    public static String encodeValue(String value) {
        return Base64.getUrlEncoder().encodeToString(value.getBytes());
    }

    public static String decodeValue(String encodeValue) {
        return new String(Base64.getUrlDecoder().decode(encodeValue));
    }
}
