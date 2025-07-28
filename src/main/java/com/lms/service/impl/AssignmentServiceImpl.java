package com.lms.service.impl;

import com.lms.model.Assignment;
import com.lms.model.Course;
import com.lms.repository.AssignmentRepository;
import com.lms.repository.CourseRepository;
import com.lms.service.AssignmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    
    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    
    public AssignmentServiceImpl(AssignmentRepository assignmentRepository,
                               CourseRepository courseRepository) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
    }
    
    @Override
    @Transactional
    public Assignment createAssignment(Assignment assignment) {
        validateAssignment(assignment);
        return assignmentRepository.save(assignment);
    }
    
    @Override
    @Transactional
    public Assignment updateAssignment(Assignment assignment) {
        validateAssignment(assignment);
        if (!assignmentRepository.existsById(assignment.getId())) {
            throw new RuntimeException("Assignment not found");
        }
        return assignmentRepository.save(assignment);
    }
    
    @Override
    @Transactional
    public void deleteAssignment(Long assignmentId) {
        assignmentRepository.deleteById(assignmentId);
    }
    
    @Override
    public Optional<Assignment> findById(Long id) {
        return assignmentRepository.findById(id);
    }
    
    @Override
    public List<Assignment> findByCourseId(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return assignmentRepository.findByCourse(course);
    }
    
    @Override
    public List<Assignment> findUpcomingAssignments(Long courseId) {
        return assignmentRepository.findUpcomingAssignments(courseId, LocalDateTime.now());
    }
    
    @Override
    public List<Assignment> findPastAssignments(Long courseId) {
        return assignmentRepository.findPastAssignments(courseId, LocalDateTime.now());
    }
    
    @Override
    public List<Assignment> findByInstructorId(Long instructorId) {
        return assignmentRepository.findByInstructorId(instructorId);
    }
    
    @Override
    public List<Assignment> findUpcomingAssignmentsByStudentId(Long studentId) {
        return assignmentRepository.findUpcomingAssignmentsByStudentId(studentId, LocalDateTime.now());
    }
    
    private void validateAssignment(Assignment assignment) {
        if (assignment.getTitle() == null || assignment.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Assignment title cannot be empty");
        }
        if (assignment.getDeadline() == null) {
            throw new IllegalArgumentException("Assignment deadline must be set");
        }
        if (assignment.getMaxScore() == null || assignment.getMaxScore() <= 0) {
            throw new IllegalArgumentException("Assignment max score must be positive");
        }
        if (assignment.getCourse() == null) {
            throw new IllegalArgumentException("Assignment must be associated with a course");
        }
    }
}
