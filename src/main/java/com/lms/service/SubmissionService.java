package com.lms.service;

import com.lms.model.Submission;
import java.util.List;
import java.util.Optional;

public interface SubmissionService {
    Submission submitAssignment(Long assignmentId, Long userId, String content);
    Submission gradeSubmission(Long submissionId, Integer score, String feedback);
    Optional<Submission> findById(Long id);
    List<Submission> findByAssignment(Long assignmentId);
    List<Submission> findByUser(Long userId);
    Optional<Submission> findByUserAndAssignment(Long userId, Long assignmentId);
    List<Submission> findPendingSubmissions(Long assignmentId);
    List<Submission> findPendingSubmissionsByInstructor(Long instructorId);
    Double getAverageScore(Long assignmentId);
    List<Submission> findGradedSubmissionsByStudent(Long userId);
}
