package com.example.backendclonereddit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;


@Entity(name = "Comments")
public class Comment {
    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Post post;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reply> replies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Vote> votes;


    @Column(unique = false)
    @NotBlank(message = "Comment cannot be empty")
    @JsonProperty(value = "content", required = true)
    @Size(min = 1, max = 4096, message = "Comment must be between 1 and 4096 characters long")
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

    public Comment(Long id, Post post, List<Reply> replies, User user, List<Vote> votes, String content, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.post = post;
        this.replies = replies;
        this.user = user;
        this.votes = votes;
        this.content = content;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
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

}
