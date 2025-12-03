package com.esgo.backend.model;

import jakarta.persistence.*;
// @Entity tells Spring Boot: "This class represents a table in the database."
@Entity
// @Table tells it to name the table "users" (because 'user' is sometimes a reserved keyword in SQL)
@Table(name = "users")
public class User {

    // @Id means this is the Primary Key
    // @GeneratedValue means MySQL will auto-increment this ID (1, 2, 3...)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullname;
    private String email;
    
    // unique = true ensures no two people can have the same username
    @Column(unique = true) 
    private String username;
    
    private String password;
    private String role; // Stores "industry" or "investor"

    // --- GETTERS AND SETTERS ---
    // (These are needed for Spring to read/write data to these fields)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}