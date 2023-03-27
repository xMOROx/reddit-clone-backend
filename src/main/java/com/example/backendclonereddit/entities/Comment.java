package com.example.backendclonereddit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;


@Entity(name = "comments")
public class Comment {
    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Vote> votes;

    @Column(unique = false)
    @NotNull
    @Size(min = 10, max = 4096, message = "Comment must be between 10 and 4096 characters long")
    private String text;
    @Column(unique = false)
    @NotNull
    @Past
    private LocalDateTime createdDate;

    @Column(unique = false)
    @NotNull
    @Past
    private LocalDateTime lastModifiedDate;

    public Comment(Long id, String text, Post post, List<Vote> votes, User user, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.text = text;
        this.post = post;
        this.votes = votes;
        this.user = user;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public Comment() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getText() {
        return text;
    }

    public void setText(String description) {
        this.text = description;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
