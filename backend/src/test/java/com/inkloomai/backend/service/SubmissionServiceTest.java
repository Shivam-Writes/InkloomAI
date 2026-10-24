package com.inkloomai.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.inkloomai.backend.dto.CreateSubmissionRequest;
import com.inkloomai.backend.dto.DecisionRequest;
import com.inkloomai.backend.dto.ResubmitRequest;
import com.inkloomai.backend.model.Submission;
import com.inkloomai.backend.repository.SubmissionRepository;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {
    @Mock
    private SubmissionRepository repository;
    private SubmissionService service;

    @BeforeEach
    void setUp() {
        service = new SubmissionService(repository);
        lenient().when(repository.save(any(Submission.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void createStartsAtVersionOneAndAddsSubmissionEvent() {
        CreateSubmissionRequest request = new CreateSubmissionRequest(
                "Launch script", "northstar", "Northstar Devices", "nova-book-pro",
                "NovaBook Pro 14", "NBP-14", "Script / ad copy",
                "A sample draft", "Creator One", "creator@example.test", List.of());
        Submission created = service.create(request);

        assertNotNull(created.getId());
        assertEquals("Awaiting company review", created.getStatus());
        assertEquals(1, created.getVersion());
        assertEquals("Creator One", created.getSubmittedBy());
        assertEquals(1, created.getEvents().size());
    }

    @Test
    void reviewerMustCommentWhenRequestingChanges() {
        when(repository.findById("INK-TEST")).thenReturn(Optional.of(pendingSubmission()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.decide("INK-TEST",
                        new DecisionRequest("Request changes", " ", "Reviewer One")));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void decisionRecordsStatusFeedbackAndHistory() {
        when(repository.findById("INK-TEST")).thenReturn(Optional.of(pendingSubmission()));

        Submission reviewed = service.decide("INK-TEST",
                new DecisionRequest("Request changes",
                        "Please support the battery claim with evidence.", "Reviewer One"));

        assertEquals("Changes requested", reviewed.getStatus());
        assertEquals("Please support the battery claim with evidence.", reviewed.getFeedback());
        assertEquals(1, reviewed.getEvents().size());
        assertEquals("Requested changes", reviewed.getEvents().get(0).getAction());
    }

    @Test
    void resubmitArchivesPreviousVersionAndReturnsToQueue() {
        Submission pending = pendingSubmission();
        pending.setStatus("Changes requested");
        pending.setFeedback("Please revise the claim.");
        pending.setContent("Old draft");
        when(repository.findById("INK-TEST")).thenReturn(Optional.of(pending));

        Submission revised = service.resubmit("INK-TEST",
                new ResubmitRequest("Updated draft", "Script / ad copy", "Creator One", List.of()));

        assertEquals(2, revised.getVersion());
        assertEquals("Resubmitted for review", revised.getStatus());
        assertEquals(1, revised.getRevisions().size());
        assertEquals("Old draft", revised.getRevisions().get(0).getContent());
        assertEquals("Updated draft", revised.getContent());
    }

    @Test
    void cannotDecideACompletedSubmissionAgain() {
        Submission completed = pendingSubmission();
        completed.setStatus("Approved");
        when(repository.findById("INK-TEST")).thenReturn(Optional.of(completed));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.decide("INK-TEST", new DecisionRequest("Reject", "No", "Reviewer One")));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    private Submission pendingSubmission() {
        Submission submission = new Submission();
        submission.setId("INK-TEST");
        submission.setName("Test submission");
        submission.setStatus("Awaiting company review");
        submission.setVersion(1);
        submission.setContent("Old draft");
        submission.setCreatedAt(Instant.now());
        submission.setUpdatedAt(Instant.now());
        submission.setFindings(new ArrayList<>());
        submission.setEvents(new ArrayList<>());
        submission.setRevisions(new ArrayList<>());
        return submission;
    }
}
