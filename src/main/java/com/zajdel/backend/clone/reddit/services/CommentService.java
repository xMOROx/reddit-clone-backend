package com.zajdel.backend.clone.reddit.services;

import com.zajdel.backend.clone.reddit.entities.Comment;
import com.zajdel.backend.clone.reddit.repositories.CommentRepository;
import com.zajdel.backend.clone.reddit.repositories.PostRepository;
import com.zajdel.backend.clone.reddit.repositories.UserRepository;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }


    public Comment getCommentById(Long id) throws CommentNotFoundException {
        return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException("id-" + id));
    }

    public Comment createNewComment(Comment comment) {
        if (comment.getLastModifiedDate() == null) {
            comment.setLastModifiedDate(comment.getCreatedDate());
        }

        commentRepository.save(comment);
        return comment;
    }

    public void removeCommentById(Long id) throws CommentNotFoundException {
        getCommentById(id);
        commentRepository.deleteById(id);
    }

    public Long fullUpdateCommentById(Long id, Comment comment) {
        Comment commentToUpdate;
        try {
            commentToUpdate = getCommentById(id);
        } catch (CommentNotFoundException e) {
            createNewComment(comment);
            commentToUpdate = comment;
        }

        commentToUpdate.setPost(comment.getPost());
        commentToUpdate.setAuthor(comment.getAuthor());
        commentToUpdate.setReplies(comment.getReplies());
        commentToUpdate.setVotes(comment.getVotes());
        commentToUpdate.setCreatedDate(comment.getCreatedDate());
        commentToUpdate.setLastModifiedDate(comment.getLastModifiedDate());
        commentToUpdate.setContent(comment.getContent());

        commentRepository.save(commentToUpdate);

        return commentToUpdate.getId();
    }

    public Long partialUpdateCommentById(Long id, Comment comment) throws CommentNotFoundException {
        Comment commentToUpdate = getCommentById(id);

        if (comment.getContent() != null) {
            commentToUpdate.setContent(comment.getContent());
        }

        if (comment.getCreatedDate() != null) {
            commentToUpdate.setCreatedDate(comment.getCreatedDate());
        }

        if (comment.getLastModifiedDate() != null) {
            commentToUpdate.setLastModifiedDate(comment.getLastModifiedDate());
        }

        commentRepository.save(commentToUpdate);

        return commentToUpdate.getId();
    }

    public List<Comment> getCommentsByPostId(Long id) throws PostNotFoundException {
        postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("id-" + id));
        return commentRepository.findAllCommentsByPostId(id);
    }

    public List<Comment> getCommentsByUserId(Long id) throws UserNotFoundException {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("id-" + id));
        return commentRepository.findAllCommentsByAuthorId(id);
    }

    public Comment getCommentByIdAndUserId(Long id, Long userId) throws CommentNotFoundForUserException, UserNotFoundException {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id-" + userId));
        return commentRepository.findCommentByIdAndAuthorId(id, userId).orElseThrow(() -> new CommentNotFoundForUserException("id-" + id + " and userId-" + userId));
    }

    public Comment getCommentByIdAndPostId(Long id, Long postId) throws PostNotFoundException, CommentNotFoundForPostException {
        postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("id-" + postId));
        return commentRepository.findCommentByIdAndPostId(id, postId).orElseThrow(() -> new CommentNotFoundForPostException("id-" + id + " and postId-" + postId));
    }
    public void removeCommentByIdAndUserId(Long id, Long userId) throws CommentNotFoundForUserException, UserNotFoundException {
        getCommentByIdAndUserId(id, userId);
        commentRepository.deleteCommentByIdAndAuthorId(id, userId);
    }




}
