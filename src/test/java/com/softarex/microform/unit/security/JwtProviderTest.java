package com.softarex.microform.unit.security;

import com.softarex.microform.domain.User;
import com.softarex.microform.exception.JwtAuthenticationException;
import com.softarex.microform.security.JwtProvider;
import com.softarex.microform.security.UserDetailsServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class JwtProviderTest {
    @Autowired
    private JwtProvider jwtProvider;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private final static String EMAIL = "t@gmail.com";

    private User user;
    private String token;

    @Before
    public void init() {
        user = new User(
                1L, EMAIL, "1234", "Igor", "Shmeltsov", "375336667788"
        );

        token = jwtProvider.createToken(user.getUsername());

        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(user);
    }

    @Test
    public void testIsTokenValidShouldReturnTrueWhenValidApplied() {
        boolean result = jwtProvider.isTokenValid(token);

        Assert.assertTrue(result);
    }

    @Test(expected = JwtAuthenticationException.class)
    public void testIsTokenValidShouldThrowExceptionWhenInvalidApplied() {
        jwtProvider.isTokenValid("213t32tg.234ty.23145ty");
    }

    @Test
    public void testGetAuthenticationShouldReturnAuthentication() {
        Authentication expected = new UsernamePasswordAuthenticationToken(user, "", null);

        Authentication result = jwtProvider.getAuthentication(token);

        Assert.assertEquals(expected, result);
        Assert.assertEquals(expected.getPrincipal(), user);

        Mockito.verify(userDetailsService, Mockito.times(1)).loadUserByUsername(EMAIL);
    }
}