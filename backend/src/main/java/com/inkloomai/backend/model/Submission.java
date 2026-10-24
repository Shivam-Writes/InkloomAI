package com.inkloomai.backend.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "submissions")
public class Submission {
    @Id private String id;
    private String name;
    private String companyId;
    private String companyName;
    private String productId;
    private String productName;
    private String model;
    private String contentType;
    private String content;
    private List<SubmissionFinding> findings = new ArrayList<>();
    private int version = 1;
    private String status;
    private String submittedBy;
    private String submittedByEmail;
    private String feedback;
    private Instant createdAt;
    private Instant updatedAt;
    private List<SubmissionEvent> events = new ArrayList<>();
    private List<SubmissionRevision> revisions = new ArrayList<>();

    public Submission() {}
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public List<SubmissionFinding> getFindings() { return findings; }
    public void setFindings(List<SubmissionFinding> findings) { this.findings = findings; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(String submittedBy) { this.submittedBy = submittedBy; }
    public String getSubmittedByEmail() { return submittedByEmail; }
    public void setSubmittedByEmail(String submittedByEmail) { this.submittedByEmail = submittedByEmail; }
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public List<SubmissionEvent> getEvents() { return events; }
    public void setEvents(List<SubmissionEvent> events) { this.events = events; }
    public List<SubmissionRevision> getRevisions() { return revisions; }
    public void setRevisions(List<SubmissionRevision> revisions) { this.revisions = revisions; }
}
