package com.lms.service;

import com.lms.model.Course;
import com.lms.model.Enrollment;
import com.lms.model.User;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    Course createCourse(Course course);
    Course updateCourse(Course course);
    void deleteCourse(Long courseId);
    Optional<Course> findById(Long id);
    List<Course> findByInstructor(User instructor);
    List<Course> findByTitleContaining(String title);
    Course findByCode(String code);
    List<Course> findEnrolledCoursesByUserId(Long userId);
    Enrollment enrollStudent(Long courseId, Long studentId);
    void unenrollStudent(Long courseId, Long studentId);
    boolean isUserEnrolled(Long courseId, Long userId);
    List<Course> findAllCourses();
}
