package com.example.backendclonereddit.repositories.impl;

import com.example.backendclonereddit.entities.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PostRepositoryImpl {
    @PersistenceContext
    private EntityManager entityManager;
//    private PostRepository postRepository;

    public PostRepositoryImpl() {

    }
    @SuppressWarnings("unsued")
    public List<Post> findAllByUserId(Long userId) {
        String query = "SELECT p FROM Posts p WHERE p.user.id = :userId";
        TypedQuery<Post> typedQuery = entityManager.createQuery(query, Post.class);
        typedQuery.setParameter("userId", userId);
        return typedQuery.getResultList();
    }

    @SuppressWarnings("unsued")
    Optional<Post> findPostByIdForUserId(Long id, Long userId) {
        String query = "SELECT p FROM Posts p WHERE p.id = :id AND p.user.id = :userId";
        TypedQuery<Post> typedQuery = entityManager.createQuery(query, Post.class);
        typedQuery.setParameter("id", id);
        typedQuery.setParameter("userId", userId);
        return Optional.ofNullable(typedQuery.getSingleResult());
    }

    @SuppressWarnings("unsued")
    void deleteByIdAndUserId(Long id, Long userId) {
        String query = "DELETE FROM Posts p WHERE p.id = :id AND p.user.id = :userId";
        TypedQuery<Post> typedQuery = entityManager.createQuery(query, Post.class);
        typedQuery.setParameter("id", id);
        typedQuery.setParameter("userId", userId);
        typedQuery.executeUpdate();

    }
}
