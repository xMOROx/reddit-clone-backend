package com.zajdel.backend.clone.reddit.repositories;

import com.zajdel.backend.clone.reddit.entities.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Find all comments by author id
     * @param authorId author id
     * @return list of comments
     */
    @Transactional
    List<Comment> findAllCommentsByAuthorId(Long authorId);
    /**
     * Find comment by id and author id
     * @param id comment id
     * @param authorId author id
     * @return comment
     */
    @Transactional
    Optional<Comment> findCommentByIdAndAuthorId(Long id, Long authorId);
    /**
     * Find all comments by post id
     * @param postId post id
     * @return list of comments
     */
    @Transactional
    List<Comment> findAllCommentsByPostId(Long postId);
    /**
     * Delete comment by id and author id
     * @param id comment id
     * @param authorId author id
     */
    @Transactional
    void deleteCommentByIdAndAuthorId(Long id, Long authorId);
    /**
     * Find comment by id and post id
     * @param id comment id
     * @param postId post id
     * @return comment
     */
    @Transactional
    Optional<Comment> findCommentByIdAndPostId(Long id, Long postId);

}
