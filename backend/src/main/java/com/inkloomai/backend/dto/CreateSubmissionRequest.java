package com.inkloomai.backend.dto;

import java.util.List;
import com.inkloomai.backend.model.SubmissionFinding;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSubmissionRequest(
        @NotBlank @Size(max = 160) String name,
        @NotBlank @Size(max = 100) String companyId,
        @NotBlank @Size(max = 160) String companyName,
        @NotBlank @Size(max = 100) String productId,
        @NotBlank @Size(max = 160) String productName,
        @NotBlank @Size(max = 100) String model,
        @NotBlank @Size(max = 80) String contentType,
        @NotBlank @Size(max = 25000) String content,
        @NotBlank @Size(max = 120) String submittedBy,
        @Email @Size(max = 200) String submittedByEmail,
        List<@Valid SubmissionFinding> findings
) {}
