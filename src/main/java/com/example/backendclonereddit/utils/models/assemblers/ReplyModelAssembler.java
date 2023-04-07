package com.example.backendclonereddit.utils.models.assemblers;

import com.example.backendclonereddit.controllers.UserController;
import com.example.backendclonereddit.entities.Reply;
import com.example.backendclonereddit.entities.Vote;
import com.example.backendclonereddit.models.ReplyModel;
import com.example.backendclonereddit.controllers.ReplyController;
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
public class ReplyModelAssembler extends RepresentationModelAssemblerSupport<Reply, ReplyModel> {
    public ReplyModelAssembler() {
        super(ReplyController.class, ReplyModel.class);
    }

    @Override
    public @NotNull ReplyModel toModel(@NotNull Reply entity) {
        ReplyModel replyModel = ReplyModelAssembler.toReplyModel(entity);

        replyModel.add(linkTo(methodOn(ReplyController.class).getReplyById(entity.getId())).withSelfRel());
        replyModel.add(linkTo(methodOn(ReplyController.class).getAllReplies()).withRel("replies"));
        replyModel.add(linkTo(methodOn(UserController.class).getUserById(entity.getAuthor().getId())).withRel("author"));

        return replyModel;
    }

    @Override
    public @NotNull CollectionModel<ReplyModel> toCollectionModel(@NotNull Iterable<? extends Reply> entities) {
        CollectionModel<ReplyModel> replyModels = super.toCollectionModel(entities);
        replyModels.add(linkTo(methodOn(ReplyController.class).getAllReplies()).withSelfRel());
        return replyModels;
    }

    public static ReplyModel toReplyModel(Reply reply) {
        return ReplyModel.builder()
                .id(reply.getId())
                .content(reply.getContent())
                .parentCommentId(reply.getParentComment().getId())
                .authorId(reply.getAuthor().getId())
                .upVotes(Vote.countUpVotes(reply.getVotes()))
                .downVotes(Vote.countDownVotes(reply.getVotes()))
                .createdDate(reply.getCreatedDate())
                .lastModifiedDate(reply.getLastModifiedDate())
                .build();
    }

    public static List<ReplyModel> toReplyModel(List<Reply> replies) {
        if(replies.isEmpty()) {
            return Collections.emptyList();
        }

        return replies.stream()
                .map(ReplyModelAssembler::toReplyModel)
                .collect(Collectors.toList());
    }


}
