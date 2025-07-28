package com.lms.service;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
    void sendAssignmentNotification(Long assignmentId);
    void sendSubmissionConfirmation(Long submissionId);
    void sendGradingNotification(Long submissionId);
    void sendEnrollmentConfirmation(Long enrollmentId);
}
