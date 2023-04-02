package com.example.backendclonereddit.utils.models.assemblers;

import com.example.backendclonereddit.entities.Reply;
import com.example.backendclonereddit.models.ReplyModel;
import com.example.backendclonereddit.controllers.ReplyController;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ReplyModelAssembler extends RepresentationModelAssemblerSupport<Reply, ReplyModel> {
    public ReplyModelAssembler() {
        super(ReplyController.class, ReplyModel.class);
    }

    @Override
    public @NotNull ReplyModel toModel(@NotNull Reply entity) {
        ReplyModel replyModel = instantiateModel(entity);

//        replyModel.add(linkTo(methodOn(ReplyResource.class).getReplyById(entity.getId())).withSelfRel());
//
//        replyModel.setId(entity.getId());
//        replyModel.setText(entity.getText());
//        replyModel.setCreatedDate(entity.getCreatedDate());
//        replyModel.setLastModifiedDate(entity.getLastModifiedDate());
//        replyModel.setAuthor(UserModelAssembler.toUserModel(entity.getUser()));
//
//        replyModel.setUpVotes(Vote.countUpVotes(entity.getVotes()));
//        replyModel.setDownVotes(Vote.countDownVotes(entity.getVotes()));

        return replyModel;
    }
}
