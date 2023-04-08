package com.example.backendclonereddit.models;

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
@JsonRootName(value = "comment")
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "comments")
public class CommentModel extends RepresentationModel<CommentModel> {
    private Long id;
    private String content;
    private Long postId;
    private Long authorId;
    private Long upVotes;
    private Long downVotes;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private List<ReplyModel> replies;

}

