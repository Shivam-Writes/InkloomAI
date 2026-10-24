package com.inkloomai.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.inkloomai.backend.model.Submission;

public interface SubmissionRepository extends MongoRepository<Submission, String> {
}
