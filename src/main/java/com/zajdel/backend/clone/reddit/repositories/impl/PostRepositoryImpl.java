package com.example.backendclonereddit.repositories.impl;

import com.example.backendclonereddit.entities.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public List<Post> findAllPostsByAuthorId(Long authorId) {
        String query = "SELECT p FROM Posts p WHERE p.author.id = :userId";
        TypedQuery<Post> typedQuery = entityManager.createQuery(query, Post.class);
        typedQuery.setParameter("userId", authorId);
        return typedQuery.getResultList();
    }

    @SuppressWarnings("unsued")
    @Transactional
    public Optional<Post> findPostByIdAndAuthorId(Long id, Long authorId) {
        String query = "SELECT p FROM Posts p WHERE p.id = :id AND p.author.id = :userId";
        TypedQuery<Post> typedQuery = entityManager.createQuery(query, Post.class);
        typedQuery.setParameter("id", id);
        typedQuery.setParameter("userId", authorId);
        try {
            return Optional.of(typedQuery.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unsued")
    @Transactional
    public void deletePostByIdAndAuthorId(Long id, Long authorId) {
        String query = "DELETE FROM Posts p WHERE p.id = :id AND p.author.id = :authorId";
        Query typedQuery = entityManager.createQuery(query);
        typedQuery.setParameter("id", id);
        typedQuery.setParameter("authorId", authorId);
        typedQuery.executeUpdate();

    }
}
