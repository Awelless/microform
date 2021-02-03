package com.softarex.microform.integration;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softarex.microform.domain.User;
import com.softarex.microform.security.JwtProvider;
import org.junit.Assert;
import org.junit.Before;
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
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/db-modifiers/clear-db.sql", "/db-modifiers/create-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/db-modifiers/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().configure(
            MapperFeature.USE_ANNOTATIONS, false
    );

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${jwt.cookie}")
    private String jwtCookieName;

    private Cookie authenticationCookie;

    private static final String EMAIL = "t@gmail.com";
    private static final String JSON  = "application/json";

    private User user;

    @Before
    public void init() {
        String token = jwtProvider.createToken(EMAIL);
        authenticationCookie = new Cookie(jwtCookieName, token);

        user = new User();
        user.setEmail(EMAIL);
        user.setPassword("123456");
        user.setFirstName("Gleb");
        user.setLastName("Shilo");
        user.setPhoneNumber("375447779900");
    }

    @Test
    public void testGetMyProfileShouldReturnUserWhenAuthorized() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        get("/api/users")
                                .cookie(authenticationCookie)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON))
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();

        User user = objectMapper.readValue(content, User.class);

        Assert.assertNotNull(user);
        Assert.assertEquals(EMAIL, user.getEmail());
    }

    @Test
    public void testGetMyProfileShouldReturn403WhenUnauthorized() throws Exception {
        mockMvc
                .perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreateUserShouldCreateWhenValidApplied() throws Exception {
        String email = "tt@gmail.com";

        user.setEmail(email);

        MvcResult result = mockMvc
                .perform(
                        post("/api/users")
                                .contentType(JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON))
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();

        User userFromResponse = objectMapper.readValue(content, User.class);

        Assert.assertNotNull(userFromResponse);
        Assert.assertEquals(email, userFromResponse.getEmail());
    }

    @Test
    public void testCreateUserShouldReturn422WhenNotUniqueEmailApplied() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        post("/api/users")
                                .contentType(JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(JSON))
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();

        Map<?, ?> response = objectMapper.readValue(content, Map.class);

        Assert.assertNotNull(response);
        Assert.assertEquals("Email is already taken", response.get("error"));
    }

    @Test
    public void testCreateUserShouldReturn422WhenInvalidPasswordApplied() throws Exception {
        user.setPassword("123--=-123");

        mockMvc
                .perform(
                        post("/api/users")
                                .contentType(JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(JSON));
    }

    @Test
    public void testCreateUserShouldReturn422WhenIncorrectEmailApplied() throws Exception {
        user.setEmail("t@gm.");

        MvcResult result = mockMvc
                .perform(
                        post("/api/users")
                                .contentType(JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();

        Map<?, ?> errors = objectMapper.readValue(content, Map.class);

        Assert.assertNotNull(errors);
    }

    @Test
    public void testCreateUserShouldReturn403WhenAuthorized() throws Exception {
        user.setEmail("tt@gmail.com");

        mockMvc
                .perform(
                        post("/api/users")
                                .cookie(authenticationCookie)
                                .contentType(JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateUserShouldUpdateSafeFieldsWhenValidApplied() throws Exception {
        String firstName = "Gleb";

        user.setEmail("tt@gmail.com");

        MvcResult result = mockMvc
                .perform(
                        put("/api/users")
                                .cookie(authenticationCookie)
                                .contentType(JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON))
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();

        User userFromResponse = objectMapper.readValue(content, User.class);

        Assert.assertNotNull(userFromResponse);
        Assert.assertEquals(EMAIL, userFromResponse.getEmail());
        Assert.assertEquals(firstName, userFromResponse.getFirstName());
    }

    @Test
    public void testUpdateUserShouldReturn422WhenInvalidApplied() throws Exception {
        User user = new User() {{
            setFirstName("Gleb");
            setLastName("Shilo");
            setPhoneNumber("123");
        }};

        mockMvc
                .perform(
                        put("/api/users")
                                .cookie(authenticationCookie)
                                .contentType(JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(JSON));
    }

    @Test
    public void testUpdateUserShouldReturn403WhenUnauthorized() throws Exception {
        User user = new User() {{
            setFirstName("Gleb");
            setLastName("Shilo");
            setPhoneNumber("123");
        }};

        mockMvc
                .perform(
                        put("/api/users")
                                .contentType(JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void testChangePasswordShouldChangeWhenValidApplied() throws Exception {
        mockMvc
                .perform(
                        put("/api/users/change-password")
                                .cookie(authenticationCookie)
                                .param("oldPassword", "123456")
                                .param("newPassword", "qwerty")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testChangePasswordShouldReturn422WhenInvalidNewPasswordApplied() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        put("/api/users/change-password")
                                .cookie(authenticationCookie)
                                .param("oldPassword", "123456")
                                .param("newPassword", "qwer")
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(JSON))
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();

        Map<?, ?> errors = objectMapper.readValue(content, Map.class);

        Assert.assertNotNull(errors);
        Assert.assertEquals("Password length should be from 6 to 255", errors.get("error"));
    }

    @Test
    public void testChangePasswordShouldReturn401WhenIncorrectOldPasswordApplied() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        put("/api/users/change-password")
                                .cookie(authenticationCookie)
                                .param("oldPassword", "1234567")
                                .param("newPassword", "qwerty")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(JSON))
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();

        Map<?, ?> errors = objectMapper.readValue(content, Map.class);

        Assert.assertNotNull(errors);
        Assert.assertEquals("Invalid password", errors.get("error"));
    }

    @Test
    public void testChangePasswordShouldReturn403WhenUnauthorized() throws Exception {
        mockMvc
                .perform(
                        put("/api/users/change-password")
                                .param("oldPassword", "123456")
                                .param("newPassword", "qwerty")
                )
                .andExpect(status().isForbidden());
    }
}
