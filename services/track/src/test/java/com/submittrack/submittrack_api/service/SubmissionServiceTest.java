package com.submittrack.submittrack_api.service;

import com.submittrack.submittrack_api.model.Submission;
import com.submittrack.submittrack_api.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;
import static  org.mockito.Mockito.never;

// Turns on Mockito so @Mock and @InjectMocks work. No Spring, no database.
@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    @Mock
    private  SubmissionRepository submissionRepository; // fake repository

    @InjectMocks
    private SubmissionService submissionService; // real service, given the fake

    @Test
    void getAllSubmissions_returnsEverySubmissionFromRepository() {
        // Arrange: make sample data and tell the fake what to return
        Submission first = new Submission();
        first.setVendor("TekSystems");
        first.setClient("Capital one");

        Submission second = new Submission();
        second.setVendor("Infosys");
        second.setClient("Walmat");

        when(submissionRepository.findAll()).thenReturn(List.of(first,second));

        // Act: call the method we are testing
        List<Submission> result = submissionService.getAllSubmissions();

        // Assert: check the result, and check the service asked the repository
        assertThat(result).hasSize(2).containsExactly(first, second);
        verify(submissionRepository).findAll();
    }

    @Test
    void getSubmissionById_whenIdExists_whenIdExists_returnSubmission() {
        //Arrange
        Submission submission = new Submission();
        submission.setId(1L);
        submission.setVendor("TekSystems");

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));

        //Act
        Optional<Submission> result = submissionService.getSubmissionById(1L);

        //Assert
        assertThat(result).contains(submission);
        verify(submissionRepository).findById(1L);
    }

    @Test
    void getSubmissionById_whenIdMissing_returnsEmpty() {

        //Arrange: the fake says "nothing with id 99"
        when(submissionRepository.findById(99L)).thenReturn(Optional.empty());

        //Act
        Optional<Submission> result = submissionService.getSubmissionById(99L);

        //Assert
        assertThat(result).isEmpty();
    }

    @Test
    void createSubmission_clearsIdBeforeSaving() {

        //Arrange: the client sneakily sends an id
        Submission incoming = new Submission();
        incoming.setId(42L);
        incoming.setVendor("TekSystems");
        when(submissionRepository.save(incoming)).thenReturn(incoming);

        //Act
        submissionService.createSubmission(incoming);

        //Assert: capture what was really passed to save()
        ArgumentCaptor<Submission> captor = ArgumentCaptor.forClass(Submission.class);
        verify(submissionRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getVendor()).isEqualTo("TekSystems");
    }

    @Test
    void deleteSubmission_whenIdExists_deletesAndReturnsTrue() {
        when(submissionRepository.existsById(1L)).thenReturn(true);

        boolean deleted = submissionService.deleteSubmission(1L);

        assertThat(deleted).isTrue();
        verify(submissionRepository).deleteById(1L);
    }

    @Test
    void deleteSubmission_whenIdMissing_returnsFalseAndDeletesNothing() {
        when(submissionRepository.existsById(99L)).thenReturn(false);

        boolean deleted = submissionService.deleteSubmission(99L);

        assertThat(deleted).isFalse();
        verify(submissionRepository, never()).deleteById(99L);
    }

    @Test
    void updateSubmission_whenIdExists_copiesNewValuesAndSaves() {

        //Arrange: what's in the "database" now
        Submission existing = new Submission();
        existing.setId(1L);
        existing.setVendor("Old Vendor");
        existing.setClient("Old Client");

        //what the user sent
        Submission updated = new Submission();
        updated.setVendor("New Vendor");
        updated.setClient("New Client");

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(submissionRepository.save(existing)).thenReturn(existing);

        //Act
        Optional<Submission> result = submissionService.updateSubmission(1L, updated);

        //Assert: same row(id 1), new values
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getVendor()).isEqualTo("New Vendor");
        assertThat(result.get().getClient()).isEqualTo("New Client");
        verify(submissionRepository).save(existing);
    }

    @Test
    void updateSubmission_whenIdMissing_returnsEmptyAndSaveNothing() {
        when(submissionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Submission> result = submissionService.updateSubmission(99L, new Submission());

        assertThat(result).isEmpty();
        verify(submissionRepository, never()).save(any());
    }
}
