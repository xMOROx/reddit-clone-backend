package com.example.backendclonereddit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.List;

@Entity(name = "votes")
public class Vote {
    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Null
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Null
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Null
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @Null
    private Reply reply;
    @NotNull
    @Column(unique = false)
    @JsonProperty(value = "isUpvote", required = true)
    private boolean isUpvote;

    public Vote() {
    }

    public Vote(User user, Post post, Comment comment, Reply reply, boolean isUpvote) {
        this.user = user;
        this.post = post;
        this.comment = comment;
        this.reply = reply;
        this.isUpvote = isUpvote;
    }

    public static Long countUpVotes(List<Vote> votes) {
        return votes.stream().filter(Vote::isUpvote).count();
    }

    public static Long countDownVotes(List<Vote> votes) {
        return votes.stream().filter(vote -> !vote.isUpvote()).count();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    private boolean isUpvote() {
        return isUpvote;
    }

    public void setUpvote(boolean upvote) {
        isUpvote = upvote;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }
}
