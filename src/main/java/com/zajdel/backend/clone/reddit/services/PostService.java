package com.zajdel.backend.clone.reddit.services;

import com.zajdel.backend.clone.reddit.entities.Post;
import com.zajdel.backend.clone.reddit.repositories.PostRepository;
import com.zajdel.backend.clone.reddit.repositories.UserRepository;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.PostNotFoundException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.PostNotFoundForUserException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.UserNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) throws PostNotFoundException {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("id-" + id));
    }

    public Post createNewPost(Post post) {
        if (post.getLastModifiedDate() == null) {
            post.setLastModifiedDate(post.getCreatedDate());
        }

        postRepository.save(post);
        return post;
    }

    public void removePostById(Long id) throws PostNotFoundException {
        getPostById(id);
        postRepository.deleteById(id);
    }

    public Long fullUpdatePostById(Long postId, Post post) {
        Post postToUpdate;
        try {
            postToUpdate = getPostById(postId);
        } catch (PostNotFoundException e) {
            createNewPost(post);
            postToUpdate = post;
        }

        postToUpdate.setTitle(post.getTitle());
        postToUpdate.setDescription(post.getDescription());
        postToUpdate.setAuthor(post.getAuthor());
        postToUpdate.setComments(post.getComments());
        postToUpdate.setVotes(post.getVotes());
        postToUpdate.setCreatedDate(post.getCreatedDate());
        postToUpdate.setLastModifiedDate(post.getLastModifiedDate());
        postToUpdate.setImagesUrl(post.getImagesUrl());
        postToUpdate.setSubReddit(post.getSubReddit());

        postRepository.save(postToUpdate);

        return postToUpdate.getId();
    }

    public Long partialUpdatePostById(Long postId, @NotNull Post post) throws PostNotFoundException {
        Post postToUpdate = getPostById(postId);

        if (post.getTitle() != null) {
            postToUpdate.setTitle(post.getTitle());
        }
        if (post.getDescription() != null) {
            postToUpdate.setDescription(post.getDescription());
        }
        if (post.getCreatedDate() != null) {
            postToUpdate.setCreatedDate(post.getCreatedDate());
        }
        if (post.getLastModifiedDate() != null) {
            postToUpdate.setLastModifiedDate(post.getLastModifiedDate());
        }
        if (post.getImagesUrl() != null) {
            postToUpdate.setImagesUrl(post.getImagesUrl());
        }

        postRepository.save(postToUpdate);

        return postToUpdate.getId();
    }
    public Post getPostByIdForUserById(Long id, Long userId) throws PostNotFoundForUserException, UserNotFoundException {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id-" + userId));
        return postRepository.findPostByIdAndAuthorId(id, userId)
                .orElseThrow(() -> new PostNotFoundForUserException("Post id-" + id + "for user id-" + userId ));

    }
    public List<Post> getPostsByUserId(Long userId) throws UserNotFoundException, PostNotFoundForUserException {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id-" + userId));
        return postRepository.findAllPostsByAuthorId(userId);
    }
    public void removePostByUserId(Long id, Long userId) throws PostNotFoundForUserException, UserNotFoundException {
        getPostByIdForUserById(id, userId);
        postRepository.deletePostByIdAndAuthorId(id, userId);
    }
}
