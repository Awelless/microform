package com.softarex.microform.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softarex.microform.domain.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/db-modifiers/clear-db.sql", "/db-modifiers/create-fields.sql", "/db-modifiers/create-responses.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/db-modifiers/clear-db.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ResponseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllResponsesShouldReturnAllResponses() throws Exception {
        mockMvc
                .perform(
                        get("/api/responses")
                                .param("page", "1")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void testGetAllResponsesShouldCreateWhenValidApplied() throws Exception {
        Response response = new Response() {{
            setBody(new HashMap<>() {{
                put(2L, "false");
                put(3L, "Option 2");
            }});
        }};

        MvcResult result = mockMvc
                .perform(
                        post("/api/responses")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(response))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();

        Response responseFromResponse = objectMapper.readValue(content, Response.class);

        Assert.assertNotNull(responseFromResponse);
        Assert.assertEquals("false", responseFromResponse.getBody().get(2L));
        Assert.assertEquals("Option 2", responseFromResponse.getBody().get(3L));
    }

    @Test
    public void testGetAllResponsesShouldReturn422WhenActiveFieldsNotApplied() throws Exception {
        Response response = new Response() {{
            setBody(new HashMap<>() {{
                put(2L, "false");
            }});
        }};

        MvcResult result = mockMvc
                .perform(
                        post("/api/responses")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(response))
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();

        Map<?, ?> message = objectMapper.readValue(content, Map.class);

        Assert.assertNotNull(message);
        Assert.assertEquals("Required field is empty", message.get("error"));
    }

    @Test
    public void testGetAllResponsesShouldReturn422WhenNonExistentFieldApplied() throws Exception {
        Response response = new Response() {{
            setBody(new HashMap<>() {{
                put(2L, "false");
                put(3L, "Option 2");
                put(6L, "false");
            }});
        }};

        MvcResult result = mockMvc
                .perform(
                        post("/api/responses")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(response))
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();

        Map<?, ?> message = objectMapper.readValue(content, Map.class);

        Assert.assertNotNull(message);
        Assert.assertEquals("One of submitted fields doesn't exist", message.get("error"));
    }

    @Test
    public void testGetAllResponsesShouldReturn422WhenNonExistentFieldOptionApplied() throws Exception {
        Response response = new Response() {{
            setBody(new HashMap<>() {{
                put(2L, "false");
                put(3L, "Option 3");
            }});
        }};

        MvcResult result = mockMvc
                .perform(
                        post("/api/responses")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(response))
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();

        Map<?, ?> message = objectMapper.readValue(content, Map.class);

        Assert.assertNotNull(message);
        Assert.assertEquals("One of field options is invalid", message.get("error"));
    }

    @Test
    public void testGetAllResponsesShouldReturn422WhenBlankFieldApplied() throws Exception {
        Response response = new Response() {{
            setBody(new HashMap<>() {{
                put(2L, "");
                put(3L, "Option 3");
            }});
        }};

        MvcResult result = mockMvc
                .perform(
                        post("/api/responses")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(response))
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();

        Map<?, ?> message = objectMapper.readValue(content, Map.class);

        Assert.assertNotNull(message);
        Assert.assertEquals("One of submitted fields is blank", message.get("error"));
    }

    @Test
    public void testGetAllResponsesShouldReturn422WhenInvalidCheckboxOptionApplied() throws Exception {
        Response response = new Response() {{
            setBody(new HashMap<>() {{
                put(2L, "option123");
                put(3L, "Option 2");
            }});
        }};

        MvcResult result = mockMvc
                .perform(
                        post("/api/responses")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(response))
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();

        Map<?, ?> message = objectMapper.readValue(content, Map.class);

        Assert.assertNotNull(message);
        Assert.assertEquals("Checkbox value should be only true/false", message.get("error"));
    }
}
