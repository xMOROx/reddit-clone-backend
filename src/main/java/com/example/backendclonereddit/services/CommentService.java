package com.example.backendclonereddit.services;

import com.example.backendclonereddit.entities.Comment;
import com.example.backendclonereddit.repositories.CommentRepository;
import com.example.backendclonereddit.utils.exceptions.types.CommentNotFoundException;
import com.example.backendclonereddit.utils.exceptions.types.CommentNotFoundForUserException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    CommentRepository commentRepository;


    public CommentService(CommentRepository commentRepository) {
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

    public void remove(Long id) {
        commentRepository.deleteById(id);
    }

    public Long fullUpdate(Long id, Comment comment) {
        Comment commentToUpdate;
        try {
            commentToUpdate =  getCommentById(id);
        } catch (CommentNotFoundException e) {
            createNewComment(comment);
            commentToUpdate = comment;
        }

        commentToUpdate.setId(id);
        commentToUpdate.setPost(comment.getPost());
        commentToUpdate.setUser(comment.getUser());
        commentToUpdate.setReplies(comment.getReplies());
        commentToUpdate.setVotes(comment.getVotes());
        commentToUpdate.setCreatedDate(comment.getCreatedDate());
        commentToUpdate.setLastModifiedDate(comment.getLastModifiedDate());
        commentToUpdate.setContent(comment.getContent());

        commentRepository.save(commentToUpdate);

        return commentToUpdate.getId();
    }

    public Long partialUpdate(Long id, Comment comment) {
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

    public List<Comment> getCommentsByPostId(Long id) {
        return commentRepository.findAllByPostId(id);
    }

    public List<Comment> getCommentsByUserId(Long id) {
        return commentRepository.findAllByUserId(id);
    }

    public Comment getCommentByIdAndUserId(Long id, Long userId) throws CommentNotFoundForUserException {
        return commentRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new CommentNotFoundForUserException("id-" + id));
    }

    public void removeByUserId(Long id, Long userId) {
        commentRepository.deleteByIdAndUserId(id, userId);
    }

    public Comment getCommentsByPostIdAndCommentId(Long postId, Long commentId) {
        return commentRepository.findByIdAndPostId(postId, commentId);
    }


}
