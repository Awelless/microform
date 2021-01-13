package com.softarex.internship.security;

import org.springframework.lang.NonNull;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityUtils {

    static void clearCookies(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {
        for (Cookie cookie : request.getCookies()) {
            cookie.setValue(null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}
