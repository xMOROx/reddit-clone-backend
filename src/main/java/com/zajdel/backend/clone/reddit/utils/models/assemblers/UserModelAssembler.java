package com.zajdel.backend.clone.reddit.utils.models.assemblers;

import com.zajdel.backend.clone.reddit.controllers.UserController;
import com.zajdel.backend.clone.reddit.entities.User;
import com.zajdel.backend.clone.reddit.models.UserModel;
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
public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {

    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    public static UserModel toUserModel(User user) {
        if (user == null) {
            return null;
        }
        return UserModel.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .posts(PostModelAssembler.toPostModel(user.getPosts()))
                .comments(CommentModelAssembler.toCommentModel(user.getComments()))
                .replies(ReplyModelAssembler.toReplyModel(user.getReplies()))
                .subreddits(SubRedditModelAssembler.toSubRedditModel(user.getSubreddits()))
                .ownedSubReddits(SubRedditModelAssembler.toSubRedditModel(user.getOwnedSubreddits()))
                .build();
    }

    public static List<UserModel> toUserModel(List<User> users) {
        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        return users.stream()
                .map(UserModelAssembler::toUserModel)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull UserModel toModel(@NotNull User entity) {
        UserModel userModel = UserModelAssembler.toUserModel(entity);

        userModel.add(linkTo(methodOn(UserController.class).getUserById(entity.getId())).withSelfRel());
        userModel.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"));

        return userModel;
    }

    @Override
    public @NotNull CollectionModel<UserModel> toCollectionModel(@NotNull Iterable<? extends User> entities) {
        CollectionModel<UserModel> userModels = super.toCollectionModel(entities);
        userModels.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        return userModels;
    }

}
