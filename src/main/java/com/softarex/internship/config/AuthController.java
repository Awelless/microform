package com.softarex.internship.config;

import com.softarex.internship.domain.User;
import com.softarex.internship.security.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
        try {
            String token = authenticationService.authenticate(user);

            Cookie authCookie = new Cookie("Authorization", token);
            response.addCookie(authCookie);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (AuthenticationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

        for (Cookie cookie : request.getCookies()) {
            String cookieName = cookie.getName();
            Cookie cookieToDelete = new Cookie(cookieName, null);
            cookieToDelete.setMaxAge(0);
            response.addCookie(cookieToDelete);
        }

        logoutHandler.logout(request, response, null);
    }
}
