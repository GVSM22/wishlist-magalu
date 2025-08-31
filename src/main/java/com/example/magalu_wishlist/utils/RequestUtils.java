package com.example.magalu_wishlist.utils;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtils {

    public static String getEmail(HttpServletRequest request) {
        return (String) request.getAttribute("email");
    }

}
