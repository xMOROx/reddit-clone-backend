package com.zajdel.backend.clone.reddit.repositories;

import com.zajdel.backend.clone.reddit.entities.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * Find all posts by author id
     * @param authorId author id
     * @return list of posts
     */
    @Transactional
    List<Post> findAllPostsByAuthorId(Long authorId);
    /**
     * Find post by id and author id
     * @param id post id
     * @param authorId author id
     * @return post
     */
    @Transactional
    Optional<Post> findPostByIdAndAuthorId(Long id, Long authorId);
    /**
     * Delete post by id and author id
     * @param id post id
     * @param authorId author id
     */
    @Transactional
    void deletePostByIdAndAuthorId(Long id, Long authorId);

}
