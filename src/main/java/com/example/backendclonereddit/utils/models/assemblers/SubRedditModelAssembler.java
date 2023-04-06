package com.example.backendclonereddit.utils.models.assemblers;

import com.example.backendclonereddit.controllers.SubRedditController;
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
    public @NotNull SubRedditModel toModel(@NotNull SubReddit entity) {
        SubRedditModel subRedditModel = SubRedditModelAssembler.toSubRedditModel(entity);

        subRedditModel.add(linkTo(methodOn(SubRedditController.class).getSubRedditById(entity.getId())).withSelfRel());

        return subRedditModel;
    }

    @Override
    public @NotNull CollectionModel<SubRedditModel> toCollectionModel(@NotNull Iterable<? extends SubReddit> entities) {
        CollectionModel<SubRedditModel> subRedditModels = super.toCollectionModel(entities);

        subRedditModels.add(linkTo(methodOn(SubRedditController.class).getAllSubReddits()).withSelfRel());

        return subRedditModels;
    }

    public static SubRedditModel toSubRedditModel(@NotNull SubReddit subReddit) {
        return SubRedditModel.builder()
                .id(subReddit.getId())
                .name(subReddit.getName())
                .description(subReddit.getDescription())
                .numberOfPosts((long) subReddit.getPosts().size())
                .users(UserModelAssembler.toUserModel(subReddit.getUsers()))
                .bannerUrl(subReddit.getBannerUrl())
                .posts(PostModelAssembler.toPostModel(subReddit.getPosts()))
                .build();
    }

    public static List<SubRedditModel> toSubRedditModel(@NotNull List<SubReddit> subreddits) {
        if (subreddits.isEmpty()) {
            return Collections.emptyList();
        }

        return subreddits.stream()
                .map(SubRedditModelAssembler::toSubRedditModel)
                .collect(Collectors.toList());
    }
}
