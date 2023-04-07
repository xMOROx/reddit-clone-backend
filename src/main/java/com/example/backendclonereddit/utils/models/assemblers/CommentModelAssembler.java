package com.example.backendclonereddit.utils.models.assemblers;

import com.example.backendclonereddit.controllers.UserController;
import com.example.backendclonereddit.entities.Comment;
import com.example.backendclonereddit.entities.Vote;
import com.example.backendclonereddit.models.CommentModel;
import com.example.backendclonereddit.controllers.CommentController;
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
        CommentModel commentModel = CommentModelAssembler.toCommentModel(entity);

        commentModel.add(linkTo(methodOn(CommentController.class).getCommentById(entity.getId())).withSelfRel());
        commentModel.add(linkTo(methodOn(CommentController.class).getAllComments()).withRel("comments"));
        commentModel.add(linkTo(methodOn(UserController.class).getUserById(entity.getAuthor().getId())).withRel("author"));

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
                .content(comment.getContent())
                .postId(comment.getPost().getId())
                .authorId(comment.getAuthor().getId())
                .upVotes(Vote.countUpVotes(comment.getVotes()))
                .downVotes(Vote.countDownVotes(comment.getVotes()))
                .createdDate(comment.getCreatedDate())
                .lastModifiedDate(comment.getLastModifiedDate())
                .replies(ReplyModelAssembler.toReplyModel(comment.getReplies()))
                .build();
    }

    public static List<CommentModel> toCommentModel(List<Comment> comments) {
        if(comments.isEmpty()) {
            return Collections.emptyList();
        }

        return comments.stream()
                .map(CommentModelAssembler::toCommentModel)
                .collect(Collectors.toList());
    }

}
