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
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "user")
@Relation(collectionRelation = "users")
public class UserModel extends RepresentationModel<UserModel> {
    private Long id;
    private String username;
    private String email;
    private String password;
    private List<PostModel> posts;
    private List<VoteModel> votes;
    private List<CommentModel> comments;

}
