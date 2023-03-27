package com.example.backendclonereddit.models;

import com.example.backendclonereddit.entities.User;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "post")
@Relation(collectionRelation = "posts")
public class PostModel extends RepresentationModel<PostModel> {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private UserModel author;
    private List<VoteModel> votes;
    private List<CommentModel> comments;
}
