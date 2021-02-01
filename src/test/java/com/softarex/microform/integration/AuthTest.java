package com.softarex.microform.integration;

import com.softarex.microform.security.JwtProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class AuthTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${jwt.cookie}")
    private String jwtCookieName;

    private static final String EMAIL = "t@gmail.com";

    @Test
    @Sql(value = {"/db-modifiers/clear-db.sql", "/db-modifiers/create-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/db-modifiers/clear-db.sql",  executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testLoginShouldLoginWhenCorrectCredentialsApplied() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        post("/api/auth/login")
                                .param("email", EMAIL)
                                .param("password", "123456")
                )
                .andExpect(status().isOk())
                .andReturn();

        Cookie authorizationCookie = result.getResponse().getCookie(jwtCookieName);

        Assert.assertNotNull(authorizationCookie);
    }

    @Test
    public void testLoginShouldReturn401WhenIncorrectCredentialsApplied() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        post("/api/auth/login")
                                .param("email", EMAIL)
                                .param("password", "123456")
                )
                .andExpect(status().isUnauthorized())
                .andReturn();

        Cookie authorizationCookie = result.getResponse().getCookie(jwtCookieName);

        Assert.assertNull(authorizationCookie);
    }

    @Test
    public void testLogoutShouldLogoutWhenAuthenticated() throws Exception {
        String token = jwtProvider.createToken("t@gmail.com");

        Cookie authorizationCookie = new Cookie(jwtCookieName, token);

        MvcResult result = mockMvc
                .perform(
                        post("/api/auth/logout")
                                .cookie(authorizationCookie)
                )
                .andExpect(status().isOk())
                .andReturn();

        Cookie responseAuthorizationCookie = result.getResponse().getCookie(jwtCookieName);

        Assert.assertNotNull(responseAuthorizationCookie);
        Assert.assertNull(responseAuthorizationCookie.getValue());
        Assert.assertEquals(0, responseAuthorizationCookie.getMaxAge());
    }

    @Test
    public void testLogoutShouldReturn403WhenNotAuthenticated() throws Exception {
        mockMvc
                .perform(post("/api/auth/logout"))
                .andExpect(status().isForbidden());
    }
}
