package com.lms.repository;

import com.lms.model.Submission;
import com.lms.model.Assignment;
import com.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAssignment(Assignment assignment);
    
    List<Submission> findByUser(User user);
    
    Optional<Submission> findByUserAndAssignment(User user, Assignment assignment);
    
    @Query("SELECT s FROM Submission s WHERE s.assignment.id = :assignmentId AND s.status = 'SUBMITTED'")
    List<Submission> findPendingSubmissions(@Param("assignmentId") Long assignmentId);
    
    @Query("SELECT s FROM Submission s WHERE s.assignment.course.instructor.id = :instructorId AND s.status = 'SUBMITTED'")
    List<Submission> findPendingSubmissionsByInstructor(@Param("instructorId") Long instructorId);
    
    @Query("SELECT AVG(s.score) FROM Submission s WHERE s.assignment.id = :assignmentId AND s.status = 'GRADED'")
    Double getAverageScore(@Param("assignmentId") Long assignmentId);
    
    @Query("SELECT s FROM Submission s WHERE s.user.id = :userId AND s.status = 'GRADED'")
    List<Submission> findGradedSubmissionsByStudent(@Param("userId") Long userId);
}
