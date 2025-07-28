package com.lms.service;

import com.lms.model.User;
import com.lms.model.UserRole;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(User user);
    String authenticate(String email, String password);
    User updateRole(Long userId, UserRole role);
    Optional<User> findByEmail(String email);
    List<User> findByRole(UserRole role);
    List<User> searchUsers(String keyword);
    boolean existsByEmail(String email);
    void changePassword(Long userId, String oldPassword, String newPassword);
    Optional<User> findById(Long id);
}
