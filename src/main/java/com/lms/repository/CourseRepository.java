package com.lms.repository;

import com.lms.model.Course;
import com.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructor(User instructor);
    
    List<Course> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT c FROM Course c WHERE c.code = :code")
    Course findByCode(@Param("code") String code);
    
    @Query("SELECT c FROM Course c JOIN c.enrollments e WHERE e.user.id = :userId")
    List<Course> findEnrolledCoursesByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(e) > 0 FROM Course c JOIN c.enrollments e WHERE c.id = :courseId AND e.user.id = :userId")
    boolean isUserEnrolled(@Param("courseId") Long courseId, @Param("userId") Long userId);
}
