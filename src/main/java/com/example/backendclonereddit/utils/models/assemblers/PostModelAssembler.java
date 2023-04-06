package com.example.backendclonereddit.utils.models.assemblers;

import com.example.backendclonereddit.entities.Post;
import com.example.backendclonereddit.entities.Vote;
import com.example.backendclonereddit.models.PostModel;
import com.example.backendclonereddit.controllers.PostController;
import com.example.backendclonereddit.controllers.UserController;
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

    @Override
    public @NotNull PostModel toModel(@NotNull Post entity) {
        PostModel postModel = PostModelAssembler.toPostModel(entity);

        postModel.add(linkTo(methodOn(PostController.class).getPostById(entity.getId())).withSelfRel());

        return postModel;
    }

    @Override
    public @NotNull CollectionModel<PostModel> toCollectionModel(@NotNull Iterable<? extends Post> entities) {
        CollectionModel<PostModel> postModels = super.toCollectionModel(entities);
        postModels.add(linkTo(methodOn(PostController.class).getAllPosts()).withSelfRel());
        return postModels;
    }

    public static PostModel toPostModel(@NotNull Post post) {
        return PostModel.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .createdDate(post.getCreatedDate())
                .lastModifiedDate(post.getLastModifiedDate())
                .author(UserModelAssembler.toUserModel(post.getUser()))
                .comments(CommentModelAssembler.toCommentModel(post.getComments()))
                .subReddit(SubRedditModelAssembler.toSubRedditModel(post.getSubReddit()))
                .upVotes(Vote.countUpVotes(post.getVotes()))
                .downVotes(Vote.countDownVotes(post.getVotes()))
                .imagesUrl(post.getImagesUrl())
                .build();
    }

    public static List<PostModel> toPostModel(@NotNull List<Post> posts) {
        if(posts.isEmpty()) {
            return Collections.emptyList();
        }

        return posts.stream()
                    .map(PostModelAssembler::toPostModel)
                .collect(Collectors.toList());
    }
}
