package com.example.backendclonereddit.repositories;

import com.example.backendclonereddit.entities.Comment;
import com.example.backendclonereddit.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByAuthorId(Long authorId);

    Optional<Post> findPostByIdAndAuthorId(Long id, Long authorId);

    void deleteByIdAndAuthorId(Long id, Long authorId);

}
