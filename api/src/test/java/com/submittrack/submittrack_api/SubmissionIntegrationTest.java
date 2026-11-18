package com.submittrack.submittrack_api;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SubmissionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String VALID_JSON = """
            {
              "vendor": "TekSystems",
              "client": "Capital One",
              "role": "Java Developer",
              "employmentType": "C2C",
              "rate": 75,
              "status": "SUBMITTED"
            }
            """;

    @Test
    void createThenGetThenDelete_fullLifecycle() throws Exception {
        // 1. Create: real save into H2
        MvcResult created = mockMvc.perform(post("/api/submissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        // read the id the database gave it
        String body = created.getResponse().getContentAsString();
        long id = ((Number) JsonPath.read(body, "$.id")).longValue();

        // 2. Get: it really is in the database
        mockMvc.perform(get("/api/submissions/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendor").value("TekSystems"));

        // 3. Delete, then 4. confirm it's gone
        mockMvc.perform(delete("/api/submissions/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/submissions/" + id))
                .andExpect(status().isNotFound());
    }
}