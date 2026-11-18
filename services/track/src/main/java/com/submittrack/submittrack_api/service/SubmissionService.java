package com.submittrack.submittrack_api.service;

import com.submittrack.submittrack_api.model.Submission;
import com.submittrack.submittrack_api.repository.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;

    // Spring injects the repository (constructor injection, same as the controller)
    public SubmissionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    // SELECT * FROM submissions
    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    // SELECT ... WHERE id = ?
    public Optional<Submission> getSubmissionById(Long id) {
        return submissionRepository.findById(id);
    }

    // INSERT INTO submissions ...
    public Submission createSubmission(Submission submission) {
        submission.setId(null);   // always create a new row; the database picks the id
        return submissionRepository.save(submission);
    }

    // UPDATE submissions SET ... WHERE id = ?
    public Optional<Submission> updateSubmission(Long id, Submission updated) {
        return submissionRepository.findById(id)
                .map(existing -> {
                    existing.setVendor(updated.getVendor());
                    existing.setClient(updated.getClient());
                    existing.setRole(updated.getRole());
                    existing.setRate(updated.getRate());
                    existing.setEmploymentType(updated.getEmploymentType());
                    existing.setStatus(updated.getStatus());
                    return submissionRepository.save(existing);
                });
    }

    // DELETE FROM submissions WHERE id = ?
    // returns true if deleted, false if the id didn't exist
    public boolean deleteSubmission(Long id) {
        if (!submissionRepository.existsById(id)) {
            return false;
        }
        submissionRepository.deleteById(id);
        return true;
    }
}