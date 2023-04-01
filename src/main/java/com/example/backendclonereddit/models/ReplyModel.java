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
@JsonRootName(value = "reply")
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "replies")
public class ReplyModel extends RepresentationModel<ReplyModel> {
    private Long id;
    private String text;
    private CommentModel comment;
    private UserModel author;
    private Long upVotes;
    private Long downVotes;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private List<VoteModel> votes;
}
