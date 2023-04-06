package com.example.backendclonereddit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.ToString;

import java.util.List;

@Entity(name = "users")
@ToString(exclude = {"posts", "votes", "comments"})
public class User {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "`username` field is mandatory")
    @JsonProperty(value = "username", required = true)
    @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters long")
    private String username;

    @Column(unique = false)
    @NotBlank(message = "Password is mandatory")
    @JsonProperty(value = "password", required = true)
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters long")
    private String password;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @JsonProperty(value = "email", required = true)
    @Column(unique = true)
    private String email;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Vote> votes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Reply> replies;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    @JsonIgnore
    private List<SubReddit> subreddits;

    public User() {
    }

    public User(String username, String password, String email, List<Post> posts, List<Vote> votes, List<Comment> comments, List<Reply> replies, List<SubReddit> subreddits) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.posts = posts;
        this.votes = votes;
        this.comments = comments;
        this.replies = replies;
        this.subreddits = subreddits;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<Post> getPosts() {
        return posts;
    }
    public void setPosts(List<Post> posts) {
        this.posts = posts;
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

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public List<SubReddit> getSubreddits() {
        return subreddits;
    }

    public void setSubreddits(List<SubReddit> subreddits) {
        this.subreddits = subreddits;
    }
}
