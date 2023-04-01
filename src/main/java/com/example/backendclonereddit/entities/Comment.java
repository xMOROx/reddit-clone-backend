package com.example.backendclonereddit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity(name = "comments")
public class Comment  extends CommentTemplate {
    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Post post;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reply> replies;

    public Comment(Long id, String text,
                   Post post,
                   List<Vote> votes,
                   User user,
                   LocalDateTime createdDate,
                   LocalDateTime lastModifiedDate,
                   List<Reply> replies
                   ) {

        super(user, votes, text, createdDate, lastModifiedDate);
        this.id = id;
        this.post = post;
        this.replies = replies;
    }

    public Comment() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

}
