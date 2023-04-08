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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "reply")
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "replies")
public class ReplyModel extends RepresentationModel<ReplyModel> {
    private Long id;
    private String content;
    private Long parentCommentId;
    private Long authorId;
    private Long upVotes;
    private Long downVotes;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
