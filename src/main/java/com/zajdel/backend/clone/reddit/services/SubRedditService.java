package com.zajdel.backend.clone.reddit.services;

import com.zajdel.backend.clone.reddit.entities.SubReddit;
import com.zajdel.backend.clone.reddit.repositories.SubRedditRepository;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.SubRedditNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubRedditService {

    private final SubRedditRepository subRedditRepository;

    public SubRedditService(SubRedditRepository subRedditRepository) {
        this.subRedditRepository = subRedditRepository;
    }

    public List<SubReddit> getAllSubReddits() {
        return subRedditRepository.findAll();
    }

    public SubReddit getSubRedditById(Long id) throws SubRedditNotFoundException {
        return subRedditRepository.findById(id).orElseThrow(() -> new SubRedditNotFoundException("id-" + id));
    }

    public SubReddit createNewSubReddit(SubReddit subReddit) {
        subRedditRepository.save(subReddit);
        return subReddit;
    }

    public List<SubReddit> getAllUserSubReddits(Long userId) {
        return subRedditRepository.findAllByOwnerId(userId);
    }

    public void remove(Long id) {
        subRedditRepository.deleteById(id);
    }

    public Long fullUpdate(Long id, SubReddit subReddit) {
        SubReddit subRedditToUpdate;
        try {
            subRedditToUpdate = getSubRedditById(id);
        } catch (SubRedditNotFoundException e) {
            createNewSubReddit(subReddit);
            subRedditToUpdate = subReddit;
        }

        subRedditToUpdate.setName(subReddit.getName());
        subRedditToUpdate.setDescription(subReddit.getDescription());
        subRedditToUpdate.setPosts(subReddit.getPosts());
        subRedditToUpdate.setUsers(subReddit.getUsers());
        subRedditToUpdate.setBannerUrl(subReddit.getBannerUrl());

        subRedditRepository.save(subRedditToUpdate);

        return subRedditToUpdate.getId();
    }

    public Long partialUpdate(Long id, @NotNull SubReddit subReddit) throws SubRedditNotFoundException {
        SubReddit subRedditToUpdate = getSubRedditById(id);

        if (subReddit.getName() != null) {
            subRedditToUpdate.setName(subReddit.getName());
        }

        if (subReddit.getDescription() != null) {
            subRedditToUpdate.setDescription(subReddit.getDescription());
        }

        if (subReddit.getBannerUrl() != null) {
            subRedditToUpdate.setBannerUrl(subReddit.getBannerUrl());
        }

        subRedditRepository.save(subRedditToUpdate);

        return subRedditToUpdate.getId();
    }
}
