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
@JsonRootName(value = "subreddit")
@Relation(collectionRelation = "subreddits")
public class SubRedditModel extends RepresentationModel<SubRedditModel> {

    private Long id;
    private String name;
    private String description;
    private List<UserModel> users;
    private List<PostModel> posts;
    private String bannerUrl;
    private Long numberOfPosts;
}
