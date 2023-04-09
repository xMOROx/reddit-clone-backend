package com.zajdel.backend.clone.reddit.services;

import com.zajdel.backend.clone.reddit.entities.SubReddit;
import com.zajdel.backend.clone.reddit.repositories.SubRedditRepository;
import com.zajdel.backend.clone.reddit.repositories.UserRepository;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.SubRedditAlreadyExistsException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.SubRedditNotFoundException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.UserNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubRedditService {

    private final SubRedditRepository subRedditRepository;
    private final UserRepository userRepository;

    public SubRedditService(SubRedditRepository subRedditRepository, UserRepository userRepository) {
        this.subRedditRepository = subRedditRepository;
        this.userRepository = userRepository;
    }

    public List<SubReddit> getAllSubReddits() {
        return subRedditRepository.findAll();
    }

    public SubReddit getSubRedditById(Long id) throws SubRedditNotFoundException {
        return subRedditRepository.findById(id).orElseThrow(() -> new SubRedditNotFoundException("id-" + id));
    }

    public SubReddit createNewSubReddit(SubReddit subReddit) throws SubRedditAlreadyExistsException {
        Optional<SubReddit> subRedditByName = subRedditRepository.findSubRedditByName(subReddit.getName());

        if (subRedditByName.isPresent()) {
            throw new SubRedditAlreadyExistsException("Subreddit already exists with name: " + subReddit.getName());
        }

        subRedditRepository.save(subReddit);
        return subReddit;
    }

    public List<SubReddit> getAllUserSubReddits(Long userId) throws UserNotFoundException {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id-" + userId));
        return subRedditRepository.findAllSubredditsByOwnerId(userId);
    }

    public void removeSubRedditById(Long id) throws SubRedditNotFoundException {
        getSubRedditById(id);
        subRedditRepository.deleteById(id);
    }

    public Long fullUpdateSubRedditById(Long id, SubReddit subReddit) {
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

    public Long partialUpdateSubRedditById(Long id, @NotNull SubReddit subReddit) throws SubRedditNotFoundException {
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
