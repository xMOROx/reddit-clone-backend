package com.example.backendclonereddit.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;

@Entity(name = "votes")
public class Vote {
    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;
    @Null
    @Column(unique = false)
    private Long userId;
    @Null
    @Column(unique = false)
    private Long postId;
    @Null
    @Column(unique = false)
    private Long commentId;
    @NotNull
    @Column(unique = false)
    @PositiveOrZero(message = "Upvote count must be positive or zero")
    private Long upVoteCount;

    @NotNull
    @Column(unique = false)
    @PositiveOrZero(message = "Downvote count must be positive or zero")
    private Long downVoteCount;


    public Vote() {
    }

    public Vote(Long id, Long userId, Long postId, Long commentId,Long upVoteCount, Long downVoteCount) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.commentId = commentId;
        this.upVoteCount = upVoteCount;
        this.downVoteCount = downVoteCount;
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

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUpVoteCount() {
        return upVoteCount;
    }

    public void setUpVoteCount(Long upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public Long getDownVoteCount() {
        return downVoteCount;
    }

    public void setDownVoteCount(Long downVoteCount) {
        this.downVoteCount = downVoteCount;
    }
}
