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
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "post")
@Relation(collectionRelation = "posts")
public class PostModel extends RepresentationModel<PostModel> {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Long authorId;
    private Long upVotes;
    private Long downVotes;
    private List<CommentModel> comments;
    private List<String> imagesUrl;
    private Long subRedditId;
}
