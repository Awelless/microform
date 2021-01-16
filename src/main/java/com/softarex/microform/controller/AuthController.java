package com.softarex.microform.controller;

import com.softarex.microform.security.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response
    ) {
        try {
            authenticationService.authenticate(email, password, response);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (AuthenticationException e) {
            return ControllerUtils.getErrorResponse(e, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.logout(request, response);
    }
}
