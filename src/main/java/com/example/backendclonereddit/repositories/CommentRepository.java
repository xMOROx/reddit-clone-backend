package com.example.backendclonereddit.repositories;

import com.example.backendclonereddit.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByAuthorId(Long authorId);

    Optional<Comment> findByIdAndAuthorId(Long id, Long authorId);

    List<Comment> findAllByPostId(Long authorId);

    void deleteByIdAndAuthorId(Long id, Long authorId);

    Comment findByIdAndPostId(Long id, Long postId);

}
