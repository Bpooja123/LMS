package com.lms.repository;

import com.lms.model.Enrollment;
import com.lms.model.Course;
import com.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByUser(User user);
    
    List<Enrollment> findByCourse(Course course);
    
    Optional<Enrollment> findByUserAndCourse(User user, Course course);
    
    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId AND e.status = 'ENROLLED'")
    List<Enrollment> findActiveEnrollmentsByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.status = 'ENROLLED'")
    long countActiveEnrollments(@Param("courseId") Long courseId);
    
    @Query("SELECT e FROM Enrollment e WHERE e.user.id = :userId AND e.status = 'ENROLLED'")
    List<Enrollment> findActiveEnrollmentsByUserId(@Param("userId") Long userId);
}
