package com.example.backendclonereddit.models;

import com.example.backendclonereddit.entities.User;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "vote")
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "votes")
public class VoteModel extends RepresentationModel<VoteModel> {
    private Long id;
    private boolean isUpVote;
    private UserModel user;
    private PostModel post;
    private CommentModel comment;
}
