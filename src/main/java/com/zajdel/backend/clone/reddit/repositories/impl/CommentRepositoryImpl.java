package com.example.backendclonereddit.repositories.impl;

import com.example.backendclonereddit.entities.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class CommentRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

//    private CommentRepository commentRepository;

    public CommentRepositoryImpl() {
//        this.commentRepository = commentRepository;
    }

    @SuppressWarnings("unsued")
    @Transactional
    public List<Comment> findAllCommentsByAuthorId(Long authorId) {
        String query = "SELECT c FROM Comments c WHERE c.author.id = :userId";
        TypedQuery<Comment> typedQuery = entityManager.createQuery(query, Comment.class);
        typedQuery.setParameter("userId", authorId);
        return typedQuery.getResultList();
    }

    @SuppressWarnings("unsued")
    @Transactional
    public List<Comment> findAllCommentsByPostId(Long postId) {
        String query = "SELECT c FROM Comments c WHERE c.post.id = :postId";
        TypedQuery<Comment> typedQuery = entityManager.createQuery(query, Comment.class);
        typedQuery.setParameter("postId", postId);
        return typedQuery.getResultList();
    }

    @SuppressWarnings("unsued")
    @Transactional
    public Optional<Comment> findCommentByIdAndAuthorId(Long id, Long authorId) {
        String query = "SELECT c FROM Comments c WHERE c.id = :id AND c.author.id = :userId";
        TypedQuery<Comment> typedQuery = entityManager.createQuery(query, Comment.class);
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
    public void deleteCommentByIdAndAuthorId(Long id, Long authorId) {
        String query = "DELETE FROM Comments c WHERE c.id = :id AND c.author.id = :userId";
        Query typedQuery = entityManager.createQuery(query);
        typedQuery.setParameter("id", id);
        typedQuery.setParameter("userId", authorId);
        typedQuery.executeUpdate();
    }

    @SuppressWarnings("unsued")
    @Transactional
    public Optional<Comment> findCommentByIdAndPostId(Long id, Long postId) {
        String query = "SELECT c FROM Comments c WHERE c.post.id = :postId AND c.id = :CommentId";
        TypedQuery<Comment> typedQuery = entityManager.createQuery(query, Comment.class);
        typedQuery.setParameter("postId", postId);
        typedQuery.setParameter("CommentId", id);
        try {
            return Optional.of(typedQuery.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
