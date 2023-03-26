package com.example.backendclonereddit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;

@Entity(name = "votes")
public class Vote {
    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Comment comment;

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

    public Vote(Long id, User user,Long upVoteCount, Long downVoteCount, Post post, Comment comment) {
        this.id = id;
        this.upVoteCount = upVoteCount;
        this.downVoteCount = downVoteCount;
        this.user = user;
        this.post = post;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
