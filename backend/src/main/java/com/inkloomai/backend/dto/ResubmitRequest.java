package com.inkloomai.backend.dto;

import java.util.List;
import com.inkloomai.backend.model.SubmissionFinding;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResubmitRequest(
        @NotBlank @Size(max = 25000) String content,
        @NotBlank @Size(max = 80) String contentType,
        @NotBlank @Size(max = 120) String submittedBy,
        List<@Valid SubmissionFinding> findings
) {}
