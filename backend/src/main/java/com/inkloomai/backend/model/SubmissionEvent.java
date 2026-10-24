package com.inkloomai.backend.model;

import java.time.Instant;

public class SubmissionEvent {
    private String actor;
    private String role;
    private String action;
    private String comment;
    private Instant at;

    public SubmissionEvent() {}
    public SubmissionEvent(String actor, String role, String action, String comment, Instant at) {
        this.actor = actor; this.role = role; this.action = action; this.comment = comment; this.at = at;
    }
    public String getActor() { return actor; }
    public void setActor(String actor) { this.actor = actor; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Instant getAt() { return at; }
    public void setAt(Instant at) { this.at = at; }
}
