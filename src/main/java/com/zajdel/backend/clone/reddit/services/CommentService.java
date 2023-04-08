package com.zajdel.backend.clone.reddit.services;

import com.zajdel.backend.clone.reddit.entities.Comment;
import com.zajdel.backend.clone.reddit.repositories.CommentRepository;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.CommentNotFoundException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.CommentNotFoundForUserException;
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

    public Long partialUpdate(Long id, Comment comment) throws CommentNotFoundException {
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
        return commentRepository.findAllCommentsByPostId(id);
    }

    public List<Comment> getCommentsByUserId(Long id) {
        return commentRepository.findAllCommentsByAuthorId(id);
    }

    public Comment getCommentByIdAndUserId(Long id, Long userId) throws CommentNotFoundForUserException {
        return commentRepository.findCommentByIdAndAuthorId(id, userId).orElseThrow(() -> new CommentNotFoundForUserException("id-" + id));
    }

    public void removeByUserId(Long id, Long userId) {
        commentRepository.deleteCommentByIdAndAuthorId(id, userId);
    }

    public Comment getCommentByPostIdAndCommentId(Long postId, Long commentId) {
        return commentRepository.findCommentByIdAndPostId(commentId, postId).orElseThrow(() -> new CommentNotFoundException("id-" + commentId));
    }


}
