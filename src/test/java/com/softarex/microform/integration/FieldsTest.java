package com.softarex.microform.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softarex.microform.domain.field.Field;
import com.softarex.microform.domain.field.FieldType;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/db-modifiers/clear-db.sql", "/db-modifiers/create-user.sql", "/db-modifiers/create-fields.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/db-modifiers/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class FieldsTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${jwt.cookie}")
    private String jwtCookieName;

    private Cookie authenticationCookie;

    private static final String EMAIL = "t@gmail.com";

    @Before
    public void init() {
        String token = jwtProvider.createToken(EMAIL);
        authenticationCookie = new Cookie(jwtCookieName, token);
    }

    @Test
    public void testGetFieldsShouldReturnFieldsWhenAuthenticated() throws Exception {
        mockMvc
                .perform(
                        get("/api/fields")
                                .cookie(authenticationCookie)
                                .param("page", "1")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void testGetFieldsShouldReturn403WhenNotAuthenticated() throws Exception {
        mockMvc
                .perform(
                        get("/api/fields")
                                .param("page", "1")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetActiveFieldsShouldReturnActiveFieldsWhenAuthenticated() throws Exception {
        mockMvc
                .perform(
                        get("/api/fields/active")
                                .cookie(authenticationCookie)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void testCreateFieldShouldCreateWhenCorrectApplied() throws Exception {
        String label = "Single line";

        Field field = new Field() {{
            setLabel(label);
            setType(FieldType.SINGLE_LINE_TEXT);
            setActive(true);
            setRequired(true);
        }};

        MvcResult result = mockMvc
                .perform(
                        post("/api/fields")
                                .cookie(authenticationCookie)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(field))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Field fieldFromResponse = objectMapper.readValue(content, Field.class);

        Assert.assertNotNull(fieldFromResponse);
        Assert.assertEquals(label, fieldFromResponse.getLabel());
    }

    @Test
    public void testCreateFieldShouldReturn422WhenIncorrectApplied() throws Exception {
        Field field = new Field() {{
            setLabel("Radio");
            setType(FieldType.RADIO_BUTTON);
            setActive(true);
            setRequired(true);
        }};

        mockMvc
                .perform(
                        post("/api/fields")
                                .cookie(authenticationCookie)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(field))
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void testUpdateFieldShouldUpdateWhenCorrectApplied() throws Exception {
        String label = "Single line";

        Field field = new Field() {{
            setLabel(label);
            setType(FieldType.SINGLE_LINE_TEXT);
            setActive(true);
            setRequired(true);
        }};

        MvcResult result = mockMvc
                .perform(
                        put("/api/fields/2")
                                .cookie(authenticationCookie)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(field))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Field fieldFromResponse = objectMapper.readValue(content, Field.class);

        Assert.assertNotNull(fieldFromResponse);
        Assert.assertEquals(2, fieldFromResponse.getId().longValue());
        Assert.assertEquals(label, fieldFromResponse.getLabel());
    }

    @Test
    public void testUpdateFieldShouldReturn403WhenNotAuthenticated() throws Exception {
        Field field = new Field() {{
            setLabel("Radio");
            setType(FieldType.RADIO_BUTTON);
            setActive(true);
            setRequired(true);
        }};

        mockMvc
                .perform(
                        put("/api/fields/2")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(field))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUpdateFieldShouldReturn422WhenIncorrectApplied() throws Exception {
        Field field = new Field() {{
            setLabel("Radio");
            setType(FieldType.RADIO_BUTTON);
            setActive(true);
            setRequired(true);
        }};

        mockMvc
                .perform(
                        put("/api/fields/2")
                                .cookie(authenticationCookie)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(field))
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void testUpdateFieldShouldReturn404WhenIncorrectIdApplied() throws Exception {
        Field field = new Field() {{
            setLabel("Single line");
            setType(FieldType.SINGLE_LINE_TEXT);
            setActive(true);
            setRequired(true);
        }};

        mockMvc
                .perform(
                        put("/api/fields/1234")
                                .cookie(authenticationCookie)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(field))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void testDeleteFieldShouldDeleteWhenAuthenticated() throws Exception {
        mockMvc
                .perform(
                        delete("/api/fields/2")
                                .cookie(authenticationCookie)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteFieldShouldReturn403WhenUnauthenticated() throws Exception {
        mockMvc
                .perform(delete("/api/fields/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteFieldShouldReturn404WhenIncorrectIdApplied() throws Exception {
        mockMvc
                .perform(
                        delete("/api/fields/123")
                                .cookie(authenticationCookie)
                )
                .andExpect(status().isNotFound());
    }
}
