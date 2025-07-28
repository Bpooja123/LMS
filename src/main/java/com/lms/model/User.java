package com.lms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    @OneToMany(mappedBy = "instructor")
    private Set<Course> coursesTeaching;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Enrollment> enrollments;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Submission> submissions;
}

enum UserRole {
    STUDENT,
    INSTRUCTOR,
    ADMIN
}
