package com.example.backendclonereddit.utils.models.assemblers;

import com.example.backendclonereddit.entities.Post;
import com.example.backendclonereddit.entities.Vote;
import com.example.backendclonereddit.models.PostModel;
import com.example.backendclonereddit.resources.PostResource;
import com.example.backendclonereddit.resources.UserResource;
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
        super(PostResource.class, PostModel.class);
    }

    @Override
    public @NotNull PostModel toModel(@NotNull Post entity) {
        PostModel postModel = instantiateModel(entity);

        postModel.add(linkTo(methodOn(PostResource.class).getPostById(entity.getId())).withSelfRel());

        postModel.setId(entity.getId());
        postModel.setTitle(entity.getTitle());
        postModel.setDescription(entity.getDescription());
        postModel.setAuthor(UserModelAssembler.toUserModel(entity.getUser()));
        postModel.setComments(CommentModelAssembler.toCommentModel(entity.getComments()));
        postModel.setLastModifiedDate(entity.getLastModifiedDate());
        postModel.setCreatedDate(entity.getCreatedDate());
        postModel.setUpVotes(Vote.countUpVotes(entity.getVotes()));
        postModel.setDownVotes(Vote.countDownVotes(entity.getVotes()));

        return postModel;
    }

    @Override
    public @NotNull CollectionModel<PostModel> toCollectionModel(@NotNull Iterable<? extends Post> entities) {
        CollectionModel<PostModel> postModels = super.toCollectionModel(entities);
        postModels.add(linkTo(methodOn(PostResource.class).getAllPosts()).withSelfRel());
        return postModels;
    }

    public static PostModel toPostModel(Post post) {
        return PostModel.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .lastModifiedDate(post.getLastModifiedDate())
                .createdDate(post.getCreatedDate())
                .build();
    }

    public static List<PostModel> toPostModel(List<Post> posts) {
        if(posts.isEmpty()) {
            return Collections.emptyList();
        }

        return posts.stream()
                .map(post -> PostModel.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .description(post.getDescription())
                        .lastModifiedDate(post.getLastModifiedDate())
                        .createdDate(post.getCreatedDate())
                        .author(UserModelAssembler.toUserModel(post.getUser()))
                        .build()
                        .add(linkTo(methodOn(UserResource.class).getUserById(post.getUser().getId())).withSelfRel()))
                .collect(Collectors.toList());
    }
}
