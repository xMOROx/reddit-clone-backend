package com.example.backendclonereddit.models;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "comment")
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "comments")
public class CommentModel extends RepresentationModel<CommentModel> {
    private Long id;
    private String text;
    private PostModel post;
    private UserModel author;
    private List<VoteModel> vote;

}

