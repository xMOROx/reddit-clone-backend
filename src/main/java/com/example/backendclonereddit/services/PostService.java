package com.example.backendclonereddit.services;

import com.example.backendclonereddit.entities.Post;
import com.example.backendclonereddit.repositories.PostRepository;
import com.example.backendclonereddit.utils.exceptions.PostNotFoundException;
import com.example.backendclonereddit.utils.exceptions.PostNotFoundForUserException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) throws PostNotFoundException {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("id-" + id));
    }

    public Post createNewPost(Post post) {
        postRepository.save(post);
        return post;
    }

    public void remove(Long id) {
        postRepository.deleteById(id);
    }

    public void removeByUserId(Long id, Long userId)  {
        postRepository.deleteByIdAndUserId(id, userId);
    }

    public Long fullUpdate(Long postId, Post post) {
        Post postToUpdate;
        try {
            postToUpdate =  getPostById(postId);
        } catch (PostNotFoundException e) {
            createNewPost(post);
            postToUpdate = post;
        }

        postToUpdate.setId(postId);
        postToUpdate.setTitle(post.getTitle());
        postToUpdate.setDescription(post.getDescription());
        postToUpdate.setUser(post.getUser());
        postToUpdate.setComments(post.getComments());
        postToUpdate.setVotes(post.getVotes());
        postToUpdate.setCreatedDate(post.getCreatedDate());
        postToUpdate.setLastModifiedDate(post.getLastModifiedDate());

        postRepository.save(postToUpdate);

        return postToUpdate.getId();
    }

    public Long partialUpdate(Long postId, Post post) {
        Post postToUpdate = getPostById(postId);

        if (post.getTitle() != null) {
            postToUpdate.setTitle(post.getTitle());
        }
        if (post.getDescription() != null) {
            postToUpdate.setDescription(post.getDescription());
        }
        if (post.getUser() != null) {
            postToUpdate.setUser(post.getUser());
        }
        if (post.getComments() != null) {
            postToUpdate.setComments(post.getComments());
        }
        if (post.getVotes() != null) {
            postToUpdate.setVotes(post.getVotes());
        }
        if (post.getCreatedDate() != null) {
            postToUpdate.setCreatedDate(post.getCreatedDate());
        }
        if (post.getLastModifiedDate() != null) {
            postToUpdate.setLastModifiedDate(post.getLastModifiedDate());
        }

        postRepository.save(postToUpdate);

        return postToUpdate.getId();
    }

    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findAllByUserId(userId);
    }
    public Post getPostByIdForUserId(Long id, Long userId) throws PostNotFoundForUserException {
        return postRepository.findPostByIdAndUserId(id, userId).orElseThrow(() -> new PostNotFoundForUserException("id-" + id));
    }
}
