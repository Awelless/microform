package com.softarex.microform.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class IndexPageTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetIndexShouldReturnIndexPage() throws Exception {
        mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("MicroForm")));
    }

    @Test
    public void testGetRandomShouldReturn404Error() throws Exception {
        this.mockMvc
                .perform(get("/test"))
                .andExpect(status().isNotFound());
    }
}
