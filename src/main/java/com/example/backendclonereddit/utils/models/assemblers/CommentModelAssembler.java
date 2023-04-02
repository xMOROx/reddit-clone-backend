package com.example.backendclonereddit.utils.models.assemblers;

import com.example.backendclonereddit.entities.Comment;
import com.example.backendclonereddit.entities.Vote;
import com.example.backendclonereddit.models.CommentModel;
import com.example.backendclonereddit.controllers.CommentController;
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
public class CommentModelAssembler extends RepresentationModelAssemblerSupport<Comment, CommentModel> {
    public CommentModelAssembler() {
        super(CommentController.class, CommentModel.class);
    }

    @Override
    public @NotNull CommentModel toModel(@NotNull Comment entity) {
        CommentModel commentModel = instantiateModel(entity);

        commentModel.add(linkTo(methodOn(CommentController.class).getCommentById(entity.getId())).withSelfRel());

        commentModel.setId(entity.getId());
        commentModel.setText(entity.getText());
        commentModel.setCreatedDate(entity.getCreatedDate());
        commentModel.setLastModifiedDate(entity.getLastModifiedDate());
        commentModel.setAuthor(UserModelAssembler.toUserModel(entity.getUser()));
        commentModel.setPost(PostModelAssembler.toPostModel(entity.getPost()));

        commentModel.setUpVotes(Vote.countUpVotes(entity.getVotes()));
        commentModel.setDownVotes(Vote.countDownVotes(entity.getVotes()));


        return commentModel;
    }

    @Override
    public @NotNull CollectionModel<CommentModel> toCollectionModel(@NotNull Iterable<? extends Comment> entities) {
        CollectionModel<CommentModel> commentModels = super.toCollectionModel(entities);
        commentModels.add(linkTo(methodOn(CommentController.class).getAllComments()).withSelfRel());
        return commentModels;
    }

    public static CommentModel toCommentModel(Comment comment) {
        return CommentModel.builder()
                .id(comment.getId())
                .text(comment.getText())
                .build();
    }

    public static List<CommentModel> toCommentModel(@NotNull List<Comment> comments) {
        if(comments.isEmpty()) {
            return Collections.emptyList();
        }

        return comments.stream()
                .map(comment -> CommentModel.builder()
                        .id(comment.getId())
                        .text(comment.getText())
                        .author(UserModelAssembler.toUserModel(comment.getUser()))
                        .build()
                        .add(linkTo(methodOn(UserController.class).getUserById(comment.getUser().getId())).withSelfRel()))
                .collect(Collectors.toList());
    }

}
