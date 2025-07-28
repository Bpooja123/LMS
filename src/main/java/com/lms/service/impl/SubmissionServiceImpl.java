package com.lms.service.impl;

import com.lms.model.Assignment;
import com.lms.model.Submission;
import com.lms.model.SubmissionStatus;
import com.lms.model.User;
import com.lms.repository.AssignmentRepository;
import com.lms.repository.SubmissionRepository;
import com.lms.repository.UserRepository;
import com.lms.service.SubmissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubmissionServiceImpl implements SubmissionService {
    
    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    
    public SubmissionServiceImpl(SubmissionRepository submissionRepository,
                               AssignmentRepository assignmentRepository,
                               UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    @Transactional
    public Submission submitAssignment(Long assignmentId, Long userId, String content) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
                
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        LocalDateTime now = LocalDateTime.now();
        
        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setUser(user);
        submission.setContent(content);
        submission.setSubmissionDate(now);
        submission.setStatus(now.isAfter(assignment.getDeadline()) ? 
                SubmissionStatus.LATE : SubmissionStatus.SUBMITTED);
        
        return submissionRepository.save(submission);
    }
    
    @Override
    @Transactional
    public Submission gradeSubmission(Long submissionId, Integer score, String feedback) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
                
        if (score < 0 || score > submission.getAssignment().getMaxScore()) {
            throw new IllegalArgumentException("Invalid score");
        }
        
        submission.setScore(score);
        submission.setFeedback(feedback);
        submission.setStatus(SubmissionStatus.GRADED);
        
        return submissionRepository.save(submission);
    }
    
    @Override
    public Optional<Submission> findById(Long id) {
        return submissionRepository.findById(id);
    }
    
    @Override
    public List<Submission> findByAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        return submissionRepository.findByAssignment(assignment);
    }
    
    @Override
    public List<Submission> findByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return submissionRepository.findByUser(user);
    }
    
    @Override
    public Optional<Submission> findByUserAndAssignment(Long userId, Long assignmentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
                
        return submissionRepository.findByUserAndAssignment(user, assignment);
    }
    
    @Override
    public List<Submission> findPendingSubmissions(Long assignmentId) {
        return submissionRepository.findPendingSubmissions(assignmentId);
    }
    
    @Override
    public List<Submission> findPendingSubmissionsByInstructor(Long instructorId) {
        return submissionRepository.findPendingSubmissionsByInstructor(instructorId);
    }
    
    @Override
    public Double getAverageScore(Long assignmentId) {
        return submissionRepository.getAverageScore(assignmentId);
    }
    
    @Override
    public List<Submission> findGradedSubmissionsByStudent(Long userId) {
        return submissionRepository.findGradedSubmissionsByStudent(userId);
    }
}
