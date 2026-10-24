package com.inkloomai.backend.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.inkloomai.backend.dto.CreateSubmissionRequest;
import com.inkloomai.backend.dto.DecisionRequest;
import com.inkloomai.backend.dto.ResubmitRequest;
import com.inkloomai.backend.model.Submission;
import com.inkloomai.backend.service.SubmissionService;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {
    private final SubmissionService service;

    public SubmissionController(SubmissionService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'BRAND_MANAGER', 'CONTENT_CREATOR', 'REVIEWER')")
    public List<Submission> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String submittedBy,
            Authentication authentication) {
        boolean creator = hasRole(authentication, "CONTENT_CREATOR");
        String effectiveSubmitter = creator ? authentication.getName() : submittedBy;
        return service.list(status, effectiveSubmitter);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'BRAND_MANAGER', 'CONTENT_CREATOR', 'REVIEWER')")
    public Submission get(@PathVariable String id, Authentication authentication) {
        Submission submission = service.get(id);
        if (hasRole(authentication, "CONTENT_CREATOR")
                && !submission.getSubmittedBy().equalsIgnoreCase(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Submission not found.");
        }
        return submission;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'BRAND_MANAGER', 'CONTENT_CREATOR')")
    public ResponseEntity<Submission> create(
            @Valid @RequestBody CreateSubmissionRequest request,
            Authentication authentication) {
        CreateSubmissionRequest trusted = new CreateSubmissionRequest(
                request.name(), request.companyId(), request.companyName(), request.productId(),
                request.productName(), request.model(), request.contentType(), request.content(),
                authentication.getName(), null, request.findings());
        Submission created = service.create(trusted);
        return ResponseEntity.created(URI.create("/api/submissions/" + created.getId())).body(created);
    }

    @PostMapping("/{id}/decisions")
    @PreAuthorize("hasAnyRole('ADMIN', 'REVIEWER') or (hasRole('BRAND_MANAGER') and #p1.decision() == 'Request changes')")
    public Submission decide(
            @PathVariable String id,
            @Valid @RequestBody DecisionRequest request,
            Authentication authentication) {
        DecisionRequest trusted = new DecisionRequest(request.decision(), request.comment(), authentication.getName());
        return service.decide(id, trusted);
    }

    @PostMapping("/{id}/resubmit")
    @PreAuthorize("hasAnyRole('ADMIN', 'BRAND_MANAGER', 'CONTENT_CREATOR')")
    public Submission resubmit(
            @PathVariable String id,
            @Valid @RequestBody ResubmitRequest request,
            Authentication authentication) {
        ResubmitRequest trusted = new ResubmitRequest(
                request.content(), request.contentType(), authentication.getName(), request.findings());
        return service.resubmit(id, trusted);
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }
}
