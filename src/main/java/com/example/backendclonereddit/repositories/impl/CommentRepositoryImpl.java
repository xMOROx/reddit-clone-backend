package com.example.backendclonereddit.repositories.impl;

import com.example.backendclonereddit.entities.Comment;
import com.example.backendclonereddit.repositories.CommentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

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
    public List<Comment> findAllByUserId(Long userId) {
        String query = "SELECT c FROM Comments c WHERE c.user.id = :userId";
        TypedQuery<Comment> typedQuery = entityManager.createQuery(query, Comment.class);
        typedQuery.setParameter("userId", userId);
        return typedQuery.getResultList();
    }

    @SuppressWarnings("unsued")
    public List<Comment> findAllByPostId(Long postId) {
        String query = "SELECT c FROM Comments c WHERE c.post.id = :postId";
        TypedQuery<Comment> typedQuery = entityManager.createQuery(query, Comment.class);
        typedQuery.setParameter("postId", postId);
        return typedQuery.getResultList();
    }

    @SuppressWarnings("unsued")
    Optional<Comment> findByIdAndUserId(Long id, Long userId)
    {
        String query = "SELECT c FROM Comments c WHERE c.id = :id AND c.user.id = :userId";
        TypedQuery<Comment> typedQuery = entityManager.createQuery(query, Comment.class);
        typedQuery.setParameter("id", id);
        typedQuery.setParameter("userId", userId);
        return Optional.ofNullable(typedQuery.getSingleResult());
    }

    @SuppressWarnings("unsued")
    void deleteByIdAndUserId(Long id, Long userId) {
        String query = "DELETE FROM Comments c WHERE c.id = :id AND c.user.id = :userId";
        TypedQuery<Comment> typedQuery = entityManager.createQuery(query, Comment.class);
        typedQuery.setParameter("id", id);
        typedQuery.setParameter("userId", userId);
        typedQuery.executeUpdate();

    }

    @SuppressWarnings("unsued")
    Comment findAllCommentsByPostId(Long postId, Long CommentId) {
        String query = "SELECT c FROM Comments c WHERE c.post.id = :postId AND c.id = :CommentId";
        TypedQuery<Comment> typedQuery = entityManager.createQuery(query, Comment.class);
        typedQuery.setParameter("postId", postId);
        typedQuery.setParameter("CommentId", CommentId);
        return typedQuery.getSingleResult();
    }

}
