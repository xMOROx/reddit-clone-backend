package com.zajdel.backend.clone.reddit.utils.models.assemblers;

import com.zajdel.backend.clone.reddit.controllers.PostController;
import com.zajdel.backend.clone.reddit.controllers.UserController;
import com.zajdel.backend.clone.reddit.entities.Post;
import com.zajdel.backend.clone.reddit.entities.Vote;
import com.zajdel.backend.clone.reddit.models.PostModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PostModelAssembler extends RepresentationModelAssemblerSupport<Post, PostModel> {
    public PostModelAssembler() {
        super(PostController.class, PostModel.class);
    }

    public static PostModel toPostModel(Post post) {
        Long subRedditId = post.getSubReddit() != null ? post.getSubReddit().getId() : null;


        return PostModel.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .createdDate(post.getCreatedDate())
                .lastModifiedDate(post.getLastModifiedDate())
                .comments(CommentModelAssembler.toCommentModel(post.getComments()))
                .subRedditId(subRedditId)
                .authorId(post.getAuthor().getId())
                .upVotes(Vote.countUpVotes(post.getVotes()))
                .downVotes(Vote.countDownVotes(post.getVotes()))
                .imagesUrl(post.getImagesUrl())
                .build();
    }

    public static List<PostModel> toPostModel(List<Post> posts) {
        if (posts.isEmpty()) {
            return Collections.emptyList();
        }

        return posts.stream()
                .map(PostModelAssembler::toPostModel)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull PostModel toModel(Post entity) {
        PostModel postModel = PostModelAssembler.toPostModel(entity);

        postModel.add(linkTo(methodOn(PostController.class).getPostById(entity.getId())).withSelfRel());
        postModel.add(linkTo(methodOn(PostController.class).getAllPosts()).withRel("posts"));
        postModel.add(linkTo(methodOn(UserController.class).getUserById(entity.getAuthor().getId())).withRel("author"));

        return postModel;
    }

    @Override
    public @NotNull CollectionModel<PostModel> toCollectionModel(Iterable<? extends Post> entities) {
        CollectionModel<PostModel> postModels = super.toCollectionModel(entities);
        postModels.add(linkTo(methodOn(PostController.class).getAllPosts()).withSelfRel());
        return postModels;
    }
}
