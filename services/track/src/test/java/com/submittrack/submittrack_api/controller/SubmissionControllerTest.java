package com.submittrack.submittrack_api.controller;

import com.submittrack.submittrack_api.model.Submission;
import com.submittrack.submittrack_api.service.SubmissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@WebMvcTest(SubmissionController.class)
class SubmissionControllerTest {

    @Autowired
    private MockMvc mockMvc; // the pretend browser

    @MockitoBean
    private SubmissionService submissionService; //fake service

    @Test
    void getAll_returns200AndJsonList() throws Exception {
        Submission s = new Submission();
        s.setId(1L);
        s.setVendor("TekSystems");

        when(submissionService.getAllSubmissions()).thenReturn(List.of(s));

        mockMvc.perform(get("/api/submissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].vendor").value("TekSystems"));
    }

    @Test
    void getById_whenFound_returns200() throws Exception {
        Submission s = new Submission();
        s.setId(1L);
        s.setVendor("TekSystems");
        when(submissionService.getSubmissionById(1L)).thenReturn(Optional.of(s));

        mockMvc.perform(get("/api/submissions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.vendor").value("TekSystems"));
    }

    @Test
    void getById_whenMissing_returns404() throws Exception {
        when(submissionService.getSubmissionById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/submissions/99"))
                .andExpect(status().isNotFound());
    }

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
    void create_withValidBody_returns201() throws Exception {
        Submission saved = new Submission();
        saved.setId(5L);
        saved.setVendor("TekSystems");
        when(submissionService.createSubmission(any(Submission.class))).thenReturn(saved);

        mockMvc.perform(post("/api/submissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void create_withEmptyBody_returns400AndNeverCallsService() throws Exception {
        mockMvc.perform(post("/api/submissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verify(submissionService, never()).createSubmission(any());
    }

    @Test
    void update_whenFound_returns200() throws Exception {
        Submission updated = new Submission();
        updated.setId(1L);
        updated.setVendor("TekSystems");
        when(submissionService.updateSubmission(eq(1L), any(Submission.class)))
                .thenReturn(Optional.of(updated));

        mockMvc.perform(put("/api/submissions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendor").value("TekSystems"));
    }

    @Test
    void update_whenMissing_returns404() throws Exception {
        when(submissionService.updateSubmission(eq(99L), any(Submission.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/submissions/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_whenFound_returns204() throws Exception {
        when(submissionService.deleteSubmission(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/submissions/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_whenMissing_returns404() throws Exception {
        when(submissionService.deleteSubmission(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/submissions/99"))
                .andExpect(status().isNotFound());
    }

}
