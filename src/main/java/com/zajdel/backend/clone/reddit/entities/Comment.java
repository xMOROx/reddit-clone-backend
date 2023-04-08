package com.example.backendclonereddit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reply> replies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User author;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Vote> votes;


    @Column(unique = false)
    @NotBlank(message = "Comment cannot be empty")
    @JsonProperty(value = "content", required = true)
    @Size(min = 1, max = 4096, message = "Comment must be between 1 and 4096 characters long")
    private String content;

    @Column(unique = false)
    @NotNull(message = "Created date is mandatory")
    @JsonProperty(value = "createdDate", required = true)
    @PastOrPresent
    private LocalDateTime createdDate;

    @Column(unique = false)
    @JsonProperty(value = "lastModifiedDate", required = false)
    @PastOrPresent
    private LocalDateTime lastModifiedDate;

    public Comment(Post post, List<Reply> replies, User author, List<Vote> votes, String content, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.post = post;
        this.replies = replies;
        this.author = author;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User user) {
        this.author = user;
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
