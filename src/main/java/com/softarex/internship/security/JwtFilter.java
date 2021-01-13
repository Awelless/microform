package com.softarex.internship.security;

import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = jwtProvider.resolveToken(request);

        try {
            if (token != null && jwtProvider.isTokenValid(token)) {

                Authentication authentication = jwtProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);

        } catch (AuthenticationException | JwtException e) {
            //Clearing SecurityContext and cookies
            SecurityContextHolder.clearContext();
            SecurityUtils.clearCookies(request, response);

            //Sending 401 response
            sendErrorMessage(response, e.getMessage());
        }
    }

    private void sendErrorMessage(@NonNull HttpServletResponse response, @NonNull final String message) throws IOException {
        response.resetBuffer();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getOutputStream().print("{\"error\": \"" + message + "\"}");

        response.flushBuffer();
    }
}
