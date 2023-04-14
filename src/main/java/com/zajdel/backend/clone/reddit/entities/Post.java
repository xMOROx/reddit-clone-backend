package com.zajdel.backend.clone.reddit.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;
@Entity(name = "Posts")
@ToString(exclude = {"author", "votes", "comments"})
public class Post {
    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @Column(unique = false)
    @JsonProperty(value = "title", required = true)
    @NotBlank(message = "Title is mandatory")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters long")
    private String title;

    @Column(unique = false)
    @NotBlank(message = "Description is mandatory")
    @JsonProperty(value = "description", required = true)
    @Size(min = 10, max = 4096, message = "Description must be between 10 and 4096 characters long")
    private String description;

    @PastOrPresent
    @Column(unique = false)
    @JsonProperty(value = "createdDate", required = true)
    @NotNull(message = "Created date is mandatory")
    private LocalDateTime createdDate;

    @Column(unique = false)
    @PastOrPresent
    @JsonProperty(value = "lastModifiedDate", required = false)
    private LocalDateTime lastModifiedDate;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Vote> votes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private SubReddit subReddit;

    @JsonProperty(value = "images", required = false)
    @ElementCollection
    List<String> imagesUrl;

    public Post() {
    }

    public Post(User author, String title, String description, SubReddit subReddit ,List<Vote> votes, List<Comment> comments, List<String> imagesUrl, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.title = title;
        this.description = description;
        this.subReddit = subReddit;
        this.author = author;
        this.votes = votes;
        this.comments = comments;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.imagesUrl = imagesUrl;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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

    public SubReddit getSubReddit() {
        return subReddit;
    }

    public void setSubReddit(SubReddit subReddit) {
        this.subReddit = subReddit;
    }

    public List<String> getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(List<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
