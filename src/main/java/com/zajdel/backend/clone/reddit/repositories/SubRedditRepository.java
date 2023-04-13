package com.zajdel.backend.clone.reddit.repositories;

import com.zajdel.backend.clone.reddit.entities.SubReddit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubRedditRepository extends JpaRepository<SubReddit, Long> {
    /**
     * Find all subreddits by owner id
     * @param ownerId owner id
     * @return list of subreddits
     */
    @Transactional
    List<SubReddit> findAllSubredditsByOwnerId(Long ownerId);
    /**
     * Find subreddit by name
     * @param name subreddit name
     * @return subreddit
     */
    @Transactional
    Optional<SubReddit> findSubRedditByName(String name);
}
