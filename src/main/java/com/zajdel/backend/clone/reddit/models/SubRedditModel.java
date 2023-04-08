package com.zajdel.backend.clone.reddit.models;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
    private Long ownerId;
}
