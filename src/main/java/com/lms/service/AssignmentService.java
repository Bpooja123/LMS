package com.lms.service;

import com.lms.model.Assignment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AssignmentService {
    Assignment createAssignment(Assignment assignment);
    Assignment updateAssignment(Assignment assignment);
    void deleteAssignment(Long assignmentId);
    Optional<Assignment> findById(Long id);
    List<Assignment> findByCourseId(Long courseId);
    List<Assignment> findUpcomingAssignments(Long courseId);
    List<Assignment> findPastAssignments(Long courseId);
    List<Assignment> findByInstructorId(Long instructorId);
    List<Assignment> findUpcomingAssignmentsByStudentId(Long studentId);
}
