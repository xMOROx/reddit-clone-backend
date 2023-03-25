package com.example.backendclonereddit.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;

    @NotNull(message = "`username` field is mandatory")
    @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters long")
    @Column(unique = true)
    private String username;
    @NotBlank(message = "Password is mandatory")
    @NotNull(message = "`password` field is mandatory")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters long")
    private String password;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @NotNull(message = "`email` field is mandatory")
    @Column(unique = true)
    private String email;


    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
