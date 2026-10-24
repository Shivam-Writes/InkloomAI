package com.inkloomai.backend.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SubmissionRevision {
    private int version;
    private String content;
    private List<SubmissionFinding> findings = new ArrayList<>();
    private String status;
    private Instant updatedAt;
    private String feedback;

    public SubmissionRevision() {}
    public SubmissionRevision(int version, String content, List<SubmissionFinding> findings,
                              String status, Instant updatedAt, String feedback) {
        this.version = version;
        this.content = content;
        this.findings = findings == null ? new ArrayList<>() : new ArrayList<>(findings);
        this.status = status;
        this.updatedAt = updatedAt;
        this.feedback = feedback;
    }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public List<SubmissionFinding> getFindings() { return findings; }
    public void setFindings(List<SubmissionFinding> findings) { this.findings = findings; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
