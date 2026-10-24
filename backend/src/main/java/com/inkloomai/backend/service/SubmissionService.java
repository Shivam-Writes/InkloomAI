package com.inkloomai.backend.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.inkloomai.backend.dto.CreateSubmissionRequest;
import com.inkloomai.backend.dto.DecisionRequest;
import com.inkloomai.backend.dto.ResubmitRequest;
import com.inkloomai.backend.model.Submission;
import com.inkloomai.backend.model.SubmissionEvent;
import com.inkloomai.backend.model.SubmissionFinding;
import com.inkloomai.backend.model.SubmissionRevision;
import com.inkloomai.backend.repository.SubmissionRepository;

@Service
public class SubmissionService {
    private static final String AWAITING = "Awaiting company review";
    private static final String RESUBMITTED = "Resubmitted for review";
    private static final String CHANGES_REQUESTED = "Changes requested";
    private static final String APPROVED = "Approved";
    private static final String REJECTED = "Rejected";

    private final SubmissionRepository repository;

    public SubmissionService(SubmissionRepository repository) {
        this.repository = repository;
    }

    public List<Submission> list(String status, String submittedBy) {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt")).stream()
                .filter(item -> status == null || status.isBlank()
                        || (item.getStatus() != null && item.getStatus().equalsIgnoreCase(status.trim())))
                .filter(item -> submittedBy == null || submittedBy.isBlank()
                        || (item.getSubmittedBy() != null
                        && item.getSubmittedBy().toLowerCase(Locale.ROOT)
                                .contains(submittedBy.trim().toLowerCase(Locale.ROOT))))
                .toList();
    }

    public Submission get(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Submission not found."));
    }

    public Submission create(CreateSubmissionRequest request) {
        Instant now = Instant.now();
        Submission submission = new Submission();
        submission.setId("INK-" + UUID.randomUUID().toString().replace("-", "")
                .substring(0, 8).toUpperCase(Locale.ROOT));
        submission.setName(request.name().trim());
        submission.setCompanyId(request.companyId().trim());
        submission.setCompanyName(request.companyName().trim());
        submission.setProductId(request.productId().trim());
        submission.setProductName(request.productName().trim());
        submission.setModel(request.model().trim());
        submission.setContentType(request.contentType().trim());
        submission.setContent(request.content().trim());
        submission.setFindings(normalizeFindings(request.findings(), request.productName(), request.companyName()));
        submission.setVersion(1);
        submission.setStatus(AWAITING);
        submission.setSubmittedBy(request.submittedBy().trim());
        submission.setSubmittedByEmail(blankToNull(request.submittedByEmail()));
        submission.setFeedback("");
        submission.setCreatedAt(now);
        submission.setUpdatedAt(now);
        submission.setEvents(new ArrayList<>(List.of(new SubmissionEvent(
                submission.getSubmittedBy(), "creator", "Submitted for company review",
                "Review requested for " + submission.getProductName() + ".", now))));
        submission.setRevisions(new ArrayList<>());
        return repository.save(submission);
    }

    public Submission decide(String id, DecisionRequest request) {
        Submission submission = get(id);
        ensureReviewable(submission);

        String decision = request.decision().trim();
        String comment = blankToNull(request.comment());
        if ((decision.equals("Request changes") || decision.equals("Reject")) && comment == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A reviewer comment is required when requesting changes or rejecting a submission.");
        }

        String status = switch (decision) {
            case "Approve" -> APPROVED;
            case "Request changes" -> CHANGES_REQUESTED;
            case "Reject" -> REJECTED;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported review decision.");
        };
        String action = switch (decision) {
            case "Approve" -> "Approved submission";
            case "Request changes" -> "Requested changes";
            case "Reject" -> "Rejected submission";
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported review decision.");
        };

        Instant now = Instant.now();
        submission.setStatus(status);
        submission.setFeedback(comment == null ? "Approved by company reviewer." : comment);
        submission.setUpdatedAt(now);
        submission.getEvents().add(new SubmissionEvent(
                request.reviewerName().trim(), "reviewer", action,
                comment == null ? "Reviewed against the available product facts." : comment, now));
        return repository.save(submission);
    }

    public Submission resubmit(String id, ResubmitRequest request) {
        Submission submission = get(id);
        if (!CHANGES_REQUESTED.equals(submission.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Only submissions with requested changes can be revised and resubmitted.");
        }

        Instant now = Instant.now();
        submission.getRevisions().add(new SubmissionRevision(
                submission.getVersion(), submission.getContent(), submission.getFindings(),
                submission.getStatus(), submission.getUpdatedAt(), submission.getFeedback()));

        submission.setContent(request.content().trim());
        submission.setContentType(request.contentType().trim());
        submission.setSubmittedBy(request.submittedBy().trim());
        submission.setFindings(normalizeFindings(request.findings(), submission.getProductName(), submission.getCompanyName()));
        submission.setVersion(submission.getVersion() + 1);
        submission.setStatus(RESUBMITTED);
        submission.setFeedback("");
        submission.setUpdatedAt(now);
        submission.getEvents().add(new SubmissionEvent(
                request.submittedBy().trim(), "creator",
                "Resubmitted for company review (v" + submission.getVersion() + ")",
                "Updated content was resubmitted after reviewer feedback.", now));
        return repository.save(submission);
    }

    private void ensureReviewable(Submission submission) {
        if (!AWAITING.equals(submission.getStatus()) && !RESUBMITTED.equals(submission.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This submission already has a decision. Only pending submissions can be reviewed.");
        }
    }

    private List<SubmissionFinding> normalizeFindings(List<SubmissionFinding> input,
                                                       String productName, String companyName) {
        List<SubmissionFinding> findings = new ArrayList<>();
        if (input == null) {
            return findings;
        }
        for (SubmissionFinding original : input) {
            if (original == null) {
                continue;
            }
            SubmissionFinding finding = new SubmissionFinding();
            finding.setId(original.getId());
            finding.setSev(original.getSev());
            finding.setCategory(original.getCategory());
            finding.setClaim(original.getClaim());
            finding.setWhy(original.getWhy());
            finding.setSource(original.getSource());
            finding.setQuote(original.getQuote());
            finding.setFix(original.getFix());
            finding.setState("Needs review");
            finding.setProductName(blankToNull(original.getProductName()) == null ? productName : original.getProductName());
            finding.setCompanyName(blankToNull(original.getCompanyName()) == null ? companyName : original.getCompanyName());
            finding.setRuleType(original.getRuleType());
            findings.add(finding);
        }
        return findings;
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
