package com.example.backendclonereddit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "posts")
@ToString(exclude = {"user", "votes", "comments"})
public class Post {
    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;
    @ManyToOne()
    private User user;

    @NotNull
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters long")
    @Column(unique = false)
    private String title;

    @NotNull
    @Column(unique = false)
    @Size(min = 10, max = 4096, message = "Description must be between 10 and 4096 characters long")
    private String description;
    @NotNull
    @Past
    private LocalDateTime createdDate;
    @NotNull
    @Past
    private LocalDateTime lastModifiedDate;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;


    public Post() {
    }

    public Post(Long id, User user, String description, String url, List<Vote> votes, List<Comment> comments, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.user = user;
        this.votes = votes;
        this.comments = comments;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
