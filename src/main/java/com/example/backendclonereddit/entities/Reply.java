package com.example.backendclonereddit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "comments")
public class Reply extends CommentTemplate {
    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Comment comment;

    public Reply(Long id, User user, Comment comment, List<Vote> votes, String text, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        super(user, votes, text, createdDate, lastModifiedDate);

        this.id = id;
        this.comment = comment;
    }

    public Reply() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }


}
