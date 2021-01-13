package com.softarex.internship.security;

import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@AllArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    /**
     * Authenticates user with username and password and creates cookie with token
     */
    public void authenticate(@NonNull final String username, @NonNull final String password, @NonNull HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        String token = jwtProvider.createToken(username);

        Cookie authCookie = new Cookie("Authorization", token);
        authCookie.setPath("/");
        response.addCookie(authCookie);
    }

    /**
     * Logouts and clears all cookies
     */
    public void logout(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

        SecurityUtils.clearCookies(request, response);

        logoutHandler.logout(request, response, null);
    }
}
