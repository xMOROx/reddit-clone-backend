package com.example.backendclonereddit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public abstract class CommentTemplate {

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    protected User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    protected List<Vote> votes;

    @Column(unique = false)
    @NotNull
    @Size(min = 10, max = 4096, message = "Comment must be between 10 and 4096 characters long")
    protected String text;

    @Column(unique = false)
    @NotNull
    @Past
    protected LocalDateTime createdDate;

    @Column(unique = false)
    @NotNull
    @Past
    protected LocalDateTime lastModifiedDate;

    public CommentTemplate() {
    }

    public CommentTemplate(User user, List<Vote> votes, String text, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.user = user;
        this.votes = votes;
        this.text = text;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
