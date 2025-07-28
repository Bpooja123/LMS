package com.lms.service.impl;

import com.lms.model.Course;
import com.lms.model.Enrollment;
import com.lms.model.EnrollmentStatus;
import com.lms.model.User;
import com.lms.repository.CourseRepository;
import com.lms.repository.EnrollmentRepository;
import com.lms.repository.UserRepository;
import com.lms.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    
    public CourseServiceImpl(CourseRepository courseRepository, 
                           UserRepository userRepository,
                           EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }
    
    @Override
    @Transactional
    public Course createCourse(Course course) {
        validateCourse(course);
        return courseRepository.save(course);
    }
    
    @Override
    @Transactional
    public Course updateCourse(Course course) {
        validateCourse(course);
        if (!courseRepository.existsById(course.getId())) {
            throw new RuntimeException("Course not found");
        }
        return courseRepository.save(course);
    }
    
    @Override
    @Transactional
    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }
    
    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }
    
    @Override
    public List<Course> findByInstructor(User instructor) {
        return courseRepository.findByInstructor(instructor);
    }
    
    @Override
    public List<Course> findByTitleContaining(String title) {
        return courseRepository.findByTitleContainingIgnoreCase(title);
    }
    
    @Override
    public Course findByCode(String code) {
        return courseRepository.findByCode(code);
    }
    
    @Override
    public List<Course> findEnrolledCoursesByUserId(Long userId) {
        return courseRepository.findEnrolledCoursesByUserId(userId);
    }
    
    @Override
    @Transactional
    public Enrollment enrollStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
                
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
                
        if (enrollmentRepository.findByUserAndCourse(student, course).isPresent()) {
            throw new RuntimeException("Student already enrolled in this course");
        }
        
        Enrollment enrollment = new Enrollment();
        enrollment.setUser(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.ENROLLED);
        
        return enrollmentRepository.save(enrollment);
    }
    
    @Override
    @Transactional
    public void unenrollStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
                
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
                
        Enrollment enrollment = enrollmentRepository.findByUserAndCourse(student, course)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
                
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        enrollmentRepository.save(enrollment);
    }
    
    @Override
    public boolean isUserEnrolled(Long courseId, Long userId) {
        return courseRepository.isUserEnrolled(courseId, userId);
    }
    
    @Override
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }
    
    private void validateCourse(Course course) {
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty");
        }
        if (course.getCode() == null || course.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Course code cannot be empty");
        }
        if (course.getInstructor() == null) {
            throw new IllegalArgumentException("Course must have an instructor");
        }
    }
}
