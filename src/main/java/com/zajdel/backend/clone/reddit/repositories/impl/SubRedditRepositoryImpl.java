package com.example.backendclonereddit.repositories.impl;

import com.example.backendclonereddit.entities.SubReddit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubRedditRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public SubRedditRepositoryImpl() {
    }

    @SuppressWarnings("unsued")
    @Transactional
    public List<SubReddit> findAllByOwnerId(Long ownerId) {
        String query = "SELECT s FROM SubReddit s WHERE s.owner.id = :userId";

        return entityManager.createQuery(query, SubReddit.class)
                .setParameter("userId", ownerId)
                .getResultList();
    }

}
