package com.zajdel.backend.clone.reddit.repositories.impl;

import com.zajdel.backend.clone.reddit.entities.SubReddit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SubRedditRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public SubRedditRepositoryImpl() {
    }

    @SuppressWarnings("unsued")
    @Transactional
    public List<SubReddit> findAllSubredditsByOwnerId(Long ownerId) {
        String query = "SELECT s FROM SubReddit s WHERE s.owner.id = :userId";

        return entityManager.createQuery(query, SubReddit.class)
                .setParameter("userId", ownerId)
                .getResultList();
    }


    @SuppressWarnings("unsued")
    @Transactional
    public Optional<SubReddit> findSubRedditByName(String name) {
        String query = "SELECT s FROM SubReddit s WHERE s.name = :name";
        TypedQuery<SubReddit> typedQuery = entityManager.createQuery(query, SubReddit.class);
        typedQuery.setParameter("name", name);
        try {
            return Optional.of(typedQuery.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
