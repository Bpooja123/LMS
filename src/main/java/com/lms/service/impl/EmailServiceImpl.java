package com.lms.service.impl;

import com.lms.model.*;
import com.lms.repository.*;
import com.lms.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailServiceImpl implements EmailService {
    
    private final JavaMailSender mailSender;
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final EnrollmentRepository enrollmentRepository;
    
    public EmailServiceImpl(JavaMailSender mailSender,
                          AssignmentRepository assignmentRepository,
                          SubmissionRepository submissionRepository,
                          EnrollmentRepository enrollmentRepository) {
        this.mailSender = mailSender;
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
        this.enrollmentRepository = enrollmentRepository;
    }
    
    @Override
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
    
    @Override
    @Transactional(readOnly = true)
    public void sendAssignmentNotification(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
                
        Course course = assignment.getCourse();
        for (Enrollment enrollment : course.getEnrollments()) {
            if (enrollment.getStatus() == EnrollmentStatus.ENROLLED) {
                String content = String.format(
                    "New assignment posted for %s\n\n" +
                    "Title: %s\n" +
                    "Due Date: %s\n" +
                    "Description: %s",
                    course.getTitle(),
                    assignment.getTitle(),
                    assignment.getDeadline(),
                    assignment.getDescription()
                );
                
                sendEmail(enrollment.getUser().getEmail(),
                         "New Assignment Posted",
                         content);
            }
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public void sendSubmissionConfirmation(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
                
        String content = String.format(
            "Your submission for %s has been received.\n\n" +
            "Course: %s\n" +
            "Submission Date: %s\n" +
            "Status: %s",
            submission.getAssignment().getTitle(),
            submission.getAssignment().getCourse().getTitle(),
            submission.getSubmissionDate(),
            submission.getStatus()
        );
        
        sendEmail(submission.getUser().getEmail(),
                 "Assignment Submission Confirmation",
                 content);
    }
    
    @Override
    @Transactional(readOnly = true)
    public void sendGradingNotification(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
                
        String content = String.format(
            "Your submission for %s has been graded.\n\n" +
            "Course: %s\n" +
            "Score: %d/%d\n" +
            "Feedback: %s",
            submission.getAssignment().getTitle(),
            submission.getAssignment().getCourse().getTitle(),
            submission.getScore(),
            submission.getAssignment().getMaxScore(),
            submission.getFeedback()
        );
        
        sendEmail(submission.getUser().getEmail(),
                 "Assignment Graded",
                 content);
    }
    
    @Override
    @Transactional(readOnly = true)
    public void sendEnrollmentConfirmation(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
                
        String content = String.format(
            "You have been enrolled in %s.\n\n" +
            "Instructor: %s %s\n" +
            "Course Code: %s\n" +
            "Credits: %d",
            enrollment.getCourse().getTitle(),
            enrollment.getCourse().getInstructor().getFirstName(),
            enrollment.getCourse().getInstructor().getLastName(),
            enrollment.getCourse().getCode(),
            enrollment.getCourse().getCredits()
        );
        
        sendEmail(enrollment.getUser().getEmail(),
                 "Course Enrollment Confirmation",
                 content);
    }
}
