package com.example.backendclonereddit.repositories;

import com.example.backendclonereddit.entities.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllCommentsByAuthorId(Long authorId);
    Optional<Comment> findCommentByIdAndAuthorId(Long id, Long authorId);
    List<Comment> findAllCommentsByPostId(Long authorId);
    @Transactional
    void deleteCommentByIdAndAuthorId(Long id, Long authorId);
    Optional<Comment> findCommentByIdAndPostId(Long id, Long postId);

}
