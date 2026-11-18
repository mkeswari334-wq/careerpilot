package com.submittrack.submittrack_api.repository;

import com.submittrack.submittrack_api.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}
