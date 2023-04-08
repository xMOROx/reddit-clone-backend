package com.zajdel.backend.clone.reddit.models;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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

