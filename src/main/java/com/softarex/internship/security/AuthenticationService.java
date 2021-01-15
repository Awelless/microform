package com.softarex.internship.security;

import com.softarex.internship.domain.User;
import com.softarex.internship.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    /**
     * Checks if email and password valid and creates cookie with token
     */
    public void authenticate(@NonNull final String email, @NonNull final String password, @NonNull HttpServletResponse response) {
        User user = userRepository.findByEmail(email);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Email or password is invalid");
        }

        String token = jwtProvider.createToken(email);

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
