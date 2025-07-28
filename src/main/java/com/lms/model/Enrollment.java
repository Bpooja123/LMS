package com.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "enrollments")
public class Enrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    
    @Column(nullable = false)
    private LocalDateTime enrollmentDate;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;
    
    private Double grade;
}

enum EnrollmentStatus {
    ENROLLED,
    COMPLETED,
    DROPPED,
    WITHDRAWN
}
