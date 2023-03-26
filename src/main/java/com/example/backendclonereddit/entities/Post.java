package com.example.backendclonereddit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity(name = "posts")
public class Post {
    @Id
    @NotNull
    @GeneratedValue
    @Column(unique = true)
    private Long id;
    @ManyToOne()
    @JsonIgnore
    private User user;

    @NotNull
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters long")
    @Column(unique = false)
    private String title;

    @NotNull
    @Column(unique = true)
    @Size(min = 10, max = 4096, message = "URL must be between 10 and 4096 characters long")
    private String url;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Vote> votes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;
    public Post() {
    }

    public Post(Long id, User user, String title, String url, List<Vote> votes, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.user = user;
        this.votes = votes;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
