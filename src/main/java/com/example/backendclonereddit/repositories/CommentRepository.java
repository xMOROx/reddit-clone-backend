package com.example.backendclonereddit.repositories;

import com.example.backendclonereddit.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByUserId(Long userId);

    Optional<Comment> findByIdAndUserId(Long id, Long userId);

    List<Comment> findAllByPostId(Long postId);

    void deleteByIdAndUserId(Long id, Long userId);

    Comment findByIdAndPostId(Long id, Long postId);

}
