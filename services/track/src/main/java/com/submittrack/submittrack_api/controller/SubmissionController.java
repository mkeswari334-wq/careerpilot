package com.submittrack.submittrack_api.controller;

import com.submittrack.submittrack_api.model.Submission;
import com.submittrack.submittrack_api.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    //spring passes in the service automatically(dependency injection)
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    //Get /api/submissions -> returns all submissions as JSON
    @GetMapping
    public List<Submission> getAllSubmissions() {
        return submissionService.getAllSubmissions();
    }

    //GET /api/submissions/{id} -> one submission, or 404 if not found
    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable Long id) {
        return submissionService.getSubmissionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //POST /api/submissions -> creates a new submission, return 201 created
    @PostMapping
    public ResponseEntity<Submission> createSubmission(@Valid @RequestBody Submission submission) {
        Submission saved = submissionService.createSubmission(submission);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT /api/submissions/{id} → updates a submission, or 404 if not found
    @PutMapping("/{id}")
    public ResponseEntity<Submission> updateSubmission(@PathVariable Long id,
                                                       @Valid @RequestBody Submission submission) {
        return submissionService.updateSubmission(id, submission)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/submissions/{id} → 204 if deleted, 404 if not found
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        if (submissionService.deleteSubmission(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
