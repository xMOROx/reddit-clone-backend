package com.zajdel.backend.clone.reddit.utils.models.assemblers;

import com.zajdel.backend.clone.reddit.controllers.CommentController;
import com.zajdel.backend.clone.reddit.controllers.UserController;
import com.zajdel.backend.clone.reddit.entities.Comment;
import com.zajdel.backend.clone.reddit.entities.Vote;
import com.zajdel.backend.clone.reddit.models.CommentModel;
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
        if (comments.isEmpty()) {
            return Collections.emptyList();
        }

        return comments.stream()
                .map(CommentModelAssembler::toCommentModel)
                .collect(Collectors.toList());
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

}
