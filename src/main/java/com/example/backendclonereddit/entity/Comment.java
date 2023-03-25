package com.example.backendclonereddit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity(name = "comments")
public class Comment {
    @Id
    @NotNull
    @GeneratedValue
    @Column(unique = true)
    private Long id;
    @NotNull
    @Column(unique = false)
    private Long postId;
    @NotNull
    @Column(unique = false)
    private Long userId;
    @NotNull
    @Column(unique = false)
    private Long voteId;
    @Column(unique = false)
    @NotNull
    @Size(min = 10, max = 4096)
    private String text;

    public Comment(Long id, Long postId, Long userId, String text) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.text = text;
    }

    public Comment() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String description) {
        this.text = description;
    }


    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }
}
