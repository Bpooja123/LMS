package com.lms.repository;

import com.lms.model.User;
import com.lms.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
    List<User> findByRole(UserRole role);
    
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:keyword% OR u.lastName LIKE %:keyword% OR u.email LIKE %:keyword%")
    List<User> searchUsers(@Param("keyword") String keyword);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u JOIN u.coursesTeaching c WHERE c.id = :courseId")
    Optional<User> findInstructorByCourseId(@Param("courseId") Long courseId);
}
