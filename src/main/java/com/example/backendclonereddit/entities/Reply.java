package com.example.backendclonereddit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;


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
    private Comment parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reply", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Vote> votes;
    @Column(unique = false)
    @JsonProperty(value = "content", required = true)
    @NotBlank(message = "Reply for comment is mandatory")
    @Size(min = 1, max = 4096, message = "Reply for comment must be between 1 and 4096 characters long")
    private String content;

    @Column(unique = false)
    @NotBlank(message = "Created date is mandatory")
    @JsonProperty(value = "createdDate", required = true)
    @PastOrPresent
    private LocalDateTime createdDate;

    @Column(unique = false)
    @JsonProperty(value = "lastModifiedDate", required = false)
    @PastOrPresent
    private LocalDateTime lastModifiedDate;
    public Reply() {

    }

    public Reply(Comment parentComment, User user, List<Vote> votes, String content, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.parentComment = parentComment;
        this.user = user;
        this.votes = votes;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String text) {
        this.content = text;
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

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment comment) {
        this.parentComment = comment;
    }


}
