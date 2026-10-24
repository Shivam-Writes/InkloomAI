package com.inkloomai.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DecisionRequest(
        @NotBlank @Pattern(regexp = "Approve|Request changes|Reject") String decision,
        @Size(max = 2000) String comment,
        @NotBlank @Size(max = 120) String reviewerName
) {}
