package com.example.backendclonereddit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;


import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "replies")
public class Reply  {
    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reply", cascade = CascadeType.ALL, orphanRemoval = true)
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
    public Reply() {

    }

    public Reply(Comment comment, User user, List<Vote> votes, String text, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.comment = comment;
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



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }


}
