package com.zajdel.backend.clone.reddit.models;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "vote")
@Relation(collectionRelation = "votes")
public class VoteModel extends RepresentationModel<VoteModel> {
    private Long id;
    private Boolean isUpvote;
    private Long userId;
    private Long postId;
    private Long commentId;
}
