package com.submittrack.submittrack_api.config;

import com.submittrack.submittrack_api.model.Submission;
import com.submittrack.submittrack_api.repository.SubmissionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final SubmissionRepository submissionRepository;

    public DataLoader(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    // runs once when the app starts
    @Override
    public void run(String... args) {
        if (submissionRepository.count() == 0) {   // only when the table is empty
            submissionRepository.saveAll(List.of(
                    new Submission(null, "Centillion Infotech", "Wells Fargo",
                            "Senior React + Angular Developer", 60.0, "C2C", "Submitted"),
                    new Submission(null, "York Solutions", "Prime Therapeutics",
                            "Full Stack Developer", 65.0, "C2C", "Interview"),
                    new Submission(null, "Anblicks", "Republic",
                            "Senior React Developer", 68.0, "C2C", "Contacted")
            ));
        }
    }
}