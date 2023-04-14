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
    /**
     * Get all subreddits
     * @return all subreddits
     */
    public List<SubReddit> getAllSubReddits() {
        return subRedditRepository.findAll();
    }
    /**
     * Get subreddit by id
     * @param id subreddit id
     * @return subreddit
     * @throws SubRedditNotFoundException if subreddit not found
     */
    public SubReddit getSubRedditById(Long id) throws SubRedditNotFoundException {
        return subRedditRepository.findById(id).orElseThrow(() -> new SubRedditNotFoundException("id-" + id));
    }
    /**
     * Create new subreddit. If subreddit already exists, throw exception. Otherwise, create new subreddit.
     * @param subReddit subreddit to create
     * @return created subreddit
     * @throws SubRedditAlreadyExistsException if subreddit already exists
     */
    public Long createNewSubReddit(SubReddit subReddit) throws SubRedditAlreadyExistsException {
        Optional<SubReddit> subRedditByName = subRedditRepository.findSubRedditByName(subReddit.getName());

        if (subRedditByName.isPresent()) {
            throw new SubRedditAlreadyExistsException("Subreddit already exists with name: " + subReddit.getName());
        }

        subRedditRepository.save(subReddit);
        return subReddit.getId();
    }
    /**
     * Get all subreddits by user id
     * @param userId user id
     * @return all subreddits by user id
     * @throws UserNotFoundException if user not found
     */
    public List<SubReddit> getAllUserSubReddits(Long userId) throws UserNotFoundException {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id-" + userId));
        return subRedditRepository.findAllSubredditsByOwnerId(userId);
    }
    /**
     * Delete subreddit by id
     * @param id subreddit id
     * @throws SubRedditNotFoundException if subreddit not found
     */
    public void removeSubRedditById(Long id) throws SubRedditNotFoundException {
        getSubRedditById(id);
        subRedditRepository.deleteById(id);
    }
    /**
     * Update subreddit by id. If subreddit not found, create new subreddit
     * @param id subreddit id
     * @param subReddit subreddit to update
     * @return updated subreddit id
     * @throws SubRedditNotFoundException if subreddit not found
     */
    public Long fullUpdateSubRedditById(Long id, SubReddit subReddit) {
        SubReddit subRedditToUpdate;
        try {
            subRedditToUpdate = getSubRedditById(id);
            subRedditToUpdate.setId(id);
        } catch (SubRedditNotFoundException e) {
            Long createdId = createNewSubReddit(subReddit);
            subRedditToUpdate = subReddit;
            subRedditToUpdate.setId(createdId);
        }

        subRedditToUpdate.setName(subReddit.getName());
        subRedditToUpdate.setDescription(subReddit.getDescription());
        subRedditToUpdate.setPosts(subReddit.getPosts());
        subRedditToUpdate.setUsers(subReddit.getUsers());
        subRedditToUpdate.setBannerUrl(subReddit.getBannerUrl());

        subRedditRepository.save(subRedditToUpdate);

        return subRedditToUpdate.getId();
    }
    /**
     * Partial update subreddit by id. If subreddit not found, throw exception
     * @param id subreddit id
     * @param subReddit subreddit to update
     * @return updated subreddit id
     * @throws SubRedditNotFoundException if subreddit not found
     */
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
