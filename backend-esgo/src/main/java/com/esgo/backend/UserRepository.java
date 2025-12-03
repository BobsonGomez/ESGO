package com.esgo.backend.repository;

import com.esgo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<User, Long> gives us built-in methods like .save(), .findAll(), .findById()
public interface UserRepository extends JpaRepository<User, Long> {
    
    // We declare this method, and Spring Boot AUTOMATICALLY writes the SQL for it!
    // It's like saying "SELECT * FROM users WHERE username = ?"
    User findByUsername(String username);
}