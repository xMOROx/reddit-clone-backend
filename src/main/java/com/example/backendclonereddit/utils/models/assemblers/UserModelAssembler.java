package com.example.backendclonereddit.utils.models.assemblers;

import com.example.backendclonereddit.entities.User;
import com.example.backendclonereddit.models.UserModel;
import com.example.backendclonereddit.controllers.UserController;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler  extends RepresentationModelAssemblerSupport<User, UserModel> {

    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }
    @Override
    public @NotNull UserModel toModel(@NotNull User entity) {
        UserModel userModel = instantiateModel(entity);

        userModel.add(linkTo(methodOn(UserController.class).getUserById(entity.getId())).withSelfRel());
        userModel.setId(entity.getId());
        userModel.setUsername(entity.getUsername());
        userModel.setEmail(entity.getEmail());
        userModel.setPassword(entity.getPassword());
        userModel.setPosts(PostModelAssembler.toPostModel(entity.getPosts()));
        userModel.setComments(CommentModelAssembler.toCommentModel(entity.getComments()));

        return userModel;
    }

    @Override
    public @NotNull CollectionModel<UserModel> toCollectionModel(@NotNull Iterable<? extends User> entities) {
        CollectionModel<UserModel> userModels = super.toCollectionModel(entities);
        userModels.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        return userModels;
    }

    public static UserModel toUserModel(User user) {
        return UserModel.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }



}
