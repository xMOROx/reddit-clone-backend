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
    public List<Post> findAllByAuthorId(Long authorId) {
        String query = "SELECT p FROM Posts p WHERE p.author.id = :userId";
        TypedQuery<Post> typedQuery = entityManager.createQuery(query, Post.class);
        typedQuery.setParameter("userId", authorId);
        return typedQuery.getResultList();
    }

    @SuppressWarnings("unsued")
    Optional<Post> findPostByIdAndAuthorId(Long id, Long authorId) {
        String query = "SELECT p FROM Posts p WHERE p.id = :id AND p.author.id = :userId";
        TypedQuery<Post> typedQuery = entityManager.createQuery(query, Post.class);
        typedQuery.setParameter("id", id);
        typedQuery.setParameter("userId", authorId);
        return Optional.ofNullable(typedQuery.getSingleResult());
    }

    @SuppressWarnings("unsued")
    void deleteByIdAndAuthorId(Long id, Long authorId) {
        String query = "DELETE FROM Posts p WHERE p.id = :id AND p.author.id = :userId";
        TypedQuery<Post> typedQuery = entityManager.createQuery(query, Post.class);
        typedQuery.setParameter("id", id);
        typedQuery.setParameter("userId", authorId);
        typedQuery.executeUpdate();

    }
}
