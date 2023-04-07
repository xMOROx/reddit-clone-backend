package com.example.backendclonereddit.utils.models.assemblers;

import com.example.backendclonereddit.controllers.SubRedditController;
import com.example.backendclonereddit.controllers.UserController;
import com.example.backendclonereddit.entities.Reply;
import com.example.backendclonereddit.entities.SubReddit;
import com.example.backendclonereddit.models.SubRedditModel;
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
public class SubRedditModelAssembler extends RepresentationModelAssemblerSupport<SubReddit, SubRedditModel>{

    public SubRedditModelAssembler() {
        super(SubRedditController.class, SubRedditModel.class);
    }

    @Override
    public @NotNull SubRedditModel toModel(SubReddit entity) {
        SubRedditModel subRedditModel = SubRedditModelAssembler.toSubRedditModel(entity);

        subRedditModel.add(linkTo(methodOn(SubRedditController.class).getSubRedditById(entity.getId())).withSelfRel());
        subRedditModel.add(linkTo(methodOn(SubRedditController.class).getAllSubReddits()).withRel("subreddits"));
        subRedditModel.add(linkTo(methodOn(UserController.class).getUserById(entity.getOwner().getId())).withRel("owner"));

        return subRedditModel;
    }

    @Override
    public @NotNull CollectionModel<SubRedditModel> toCollectionModel(Iterable<? extends SubReddit> entities) {
        CollectionModel<SubRedditModel> subRedditModels = super.toCollectionModel(entities);

        subRedditModels.add(linkTo(methodOn(SubRedditController.class).getAllSubReddits()).withSelfRel());

        return subRedditModels;
    }

    public static SubRedditModel toSubRedditModel(SubReddit subReddit) {
        if (subReddit == null) {
            return null;
        }

        return SubRedditModel.builder()
                .id(subReddit.getId())
                .name(subReddit.getName())
                .description(subReddit.getDescription())
                .numberOfPosts((long) subReddit.getPosts().size())
                .users(UserModelAssembler.toUserModel(subReddit.getUsers()))
                .bannerUrl(subReddit.getBannerUrl())
                .posts(PostModelAssembler.toPostModel(subReddit.getPosts()))
                .ownerId(subReddit.getOwner().getId())
                .build();
    }

    public static List<SubRedditModel> toSubRedditModel(List<SubReddit> subreddits) {
        if (subreddits.isEmpty()) {
            return Collections.emptyList();
        }

        return subreddits.stream()
                .map(SubRedditModelAssembler::toSubRedditModel)
                .collect(Collectors.toList());
    }
}
