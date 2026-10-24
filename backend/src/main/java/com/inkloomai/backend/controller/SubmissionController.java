package com.inkloomai.backend.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Submission> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String submittedBy) {
        return service.list(status, submittedBy);
    }

    @GetMapping("/{id}")
    public Submission get(@PathVariable String id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<Submission> create(@Valid @RequestBody CreateSubmissionRequest request) {
        Submission created = service.create(request);
        return ResponseEntity.created(URI.create("/api/submissions/" + created.getId())).body(created);
    }

    @PostMapping("/{id}/decisions")
    public Submission decide(@PathVariable String id, @Valid @RequestBody DecisionRequest request) {
        return service.decide(id, request);
    }

    @PostMapping("/{id}/resubmit")
    public Submission resubmit(@PathVariable String id, @Valid @RequestBody ResubmitRequest request) {
        return service.resubmit(id, request);
    }
}
