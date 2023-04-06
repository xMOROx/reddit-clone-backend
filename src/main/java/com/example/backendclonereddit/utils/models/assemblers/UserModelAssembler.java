package com.example.backendclonereddit.utils.models.assemblers;

import com.example.backendclonereddit.entities.User;
import com.example.backendclonereddit.models.UserModel;
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
public class UserModelAssembler  extends RepresentationModelAssemblerSupport<User, UserModel> {

    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }
    @Override
    public @NotNull UserModel toModel(@NotNull User entity) {
        UserModel userModel = UserModelAssembler.toUserModel(entity);
        userModel.add(linkTo(methodOn(UserController.class).getUserById(entity.getId())).withSelfRel());
        return userModel;
    }

    @Override
    public @NotNull CollectionModel<UserModel> toCollectionModel(@NotNull Iterable<? extends User> entities) {
        CollectionModel<UserModel> userModels = super.toCollectionModel(entities);
        userModels.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        return userModels;
    }

    public static UserModel toUserModel(@NotNull User user) {
        return UserModel.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .posts(PostModelAssembler.toPostModel(user.getPosts()))
                .comments(CommentModelAssembler.toCommentModel(user.getComments()))
                .replies(ReplyModelAssembler.toReplyModel(user.getReplies()))
                .subreddits(SubRedditModelAssembler.toSubRedditModel(user.getSubreddits()))
                .build();
    }

    public static List<UserModel> toUserModel(@NotNull List<User> users) {
        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        return users.stream()
                .map(UserModelAssembler::toUserModel)
                .collect(Collectors.toList());
    }

}
