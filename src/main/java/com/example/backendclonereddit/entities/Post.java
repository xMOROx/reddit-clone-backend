package com.example.backendclonereddit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

@Entity(name = "posts")
public class Post {
    @Id
    @NotNull
    @GeneratedValue
    @Column(unique = true)
    private Long id;
    @NotNull
    @Column(unique = false)
    private Long userId;
    @NotNull
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters long")
    @Column(unique = false)
    private String title;

    @NotNull
    @Column(unique = true)
    @Size(min = 10, max = 4096, message = "URL must be between 10 and 4096 characters long")
    private String url;
    @NotNull
    @Column(unique = false)
    private Long voteId;

    @Null
    @Column(unique = false)
    private Long commentId;

    public Post() {
    }

    public Post(Long id, Long userId, String title, String url, Long voteId) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.url = url;
        this.voteId = voteId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
