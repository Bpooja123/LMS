package com.lms.repository;

import com.lms.model.Assignment;
import com.lms.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByCourse(Course course);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.id = :courseId AND a.deadline > :now")
    List<Assignment> findUpcomingAssignments(@Param("courseId") Long courseId, @Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.id = :courseId AND a.deadline < :now")
    List<Assignment> findPastAssignments(@Param("courseId") Long courseId, @Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Assignment a WHERE a.course.instructor.id = :instructorId")
    List<Assignment> findByInstructorId(@Param("instructorId") Long instructorId);
    
    @Query("SELECT a FROM Assignment a JOIN a.course c JOIN c.enrollments e " +
           "WHERE e.user.id = :userId AND a.deadline > :now")
    List<Assignment> findUpcomingAssignmentsByStudentId(@Param("userId") Long userId, @Param("now") LocalDateTime now);
}
