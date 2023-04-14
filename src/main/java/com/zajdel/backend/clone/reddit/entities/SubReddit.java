package com.zajdel.backend.clone.reddit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.ToString;

import java.util.List;

@Entity
@ToString(exclude = {"users", "posts", "owner"})
public class SubReddit {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters long")
    @NotBlank(message = "Name is mandatory")
    @JsonProperty(value = "name", required = true)
    private String name;

    @Column(nullable = false, unique = false)
    @Size(min = 10, max = 4096, message = "Description must be between 10 and 4096 characters long")
    @NotBlank(message = "Description is mandatory")
    @JsonProperty(value = "description", required = true)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_subreddit",
            joinColumns = @JoinColumn(name = "subreddit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    private List<User> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subReddit")
    @JsonIgnore
    private List<Post> posts;

    @JsonProperty(value = "bannerUrl", required = false)
    @Column(nullable = true, unique = false)
    private String bannerUrl;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    public SubReddit() {
    }

    public SubReddit(String name, String description, List<User> users, List<Post> posts, String bannerUrl, User owner) {
        this.name = name;
        this.description = description;
        this.users = users;
        this.posts = posts;
        this.bannerUrl = bannerUrl;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
