package com.softarex.internship.security;

import com.softarex.internship.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@AllArgsConstructor
public class JwtProvider {
    @Value("${jwt.header}")
    private final String jwtHeader;
    @Value("${jwt.secret}")
    private final String jwtSecret;
    @Value("${jwt.expiration}")
    private final Integer jwtValidityTime;

    private final UserDetailsService userDetailsService;

    public String createToken(@NonNull final String username) {
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtValidityTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public boolean isTokenValid(@NonNull final String token) {

        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token);

            return claims.getBody().getExpiration().before(new Date());
        } catch (JwtException e) {
            throw new JwtAuthenticationException("JWT is expired or invalid");
        }
    }

    public String resolveToken(@NonNull final HttpServletRequest request) {
        return request.getHeader(jwtHeader);
    }

    public Authentication getAuthentication(@NonNull final String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "");
    }

    public String getUsername(@NonNull final String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
