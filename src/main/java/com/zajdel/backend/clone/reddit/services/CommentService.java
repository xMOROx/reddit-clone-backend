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
    /**
     * Get all comments
     * @return List of all comments
     */
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    /**
     * Get comment by id
     * @param id Comment id
     * @return Comment
     * @throws CommentNotFoundException if comment not found
     */
    public Comment getCommentById(Long id) throws CommentNotFoundException {
        return commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException("id-" + id));
    }
    /**
     * Create new comment
     * @param comment Comment
     * @return Comment
     */
    public Long createNewComment(Comment comment) {
        if (comment.getLastModifiedDate() == null) {
            comment.setLastModifiedDate(comment.getCreatedDate());
        }

        commentRepository.save(comment);
        return comment.getId();
    }
    /**
     * Remove comment by id
     * @param id Comment id
     * @throws CommentNotFoundException if comment not found
     */
    public void removeCommentById(Long id) throws CommentNotFoundException {
        getCommentById(id);
        commentRepository.deleteById(id);
    }
    /**
     * Update comment by id or create new comment if not found
     * @param id Comment id
     * @param comment Comment
     * @return Comment id
     */
    public Long fullUpdateCommentById(Long id, Comment comment) {
        Comment commentToUpdate;
        try {
            commentToUpdate = getCommentById(id);
            commentToUpdate.setId(id);
        } catch (CommentNotFoundException e) {
            Long createdId = createNewComment(comment);
            commentToUpdate = comment;
            commentToUpdate.setId(createdId);
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

    /**
     * Update partial comment by id
     * @param id Comment id
     * @param comment Comment
     * @return Comment id
     * @throws CommentNotFoundException if comment not found
     */
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
    /**
     * Get all comments by post id
     * @param id Post id
     * @return List of comments
     * @throws PostNotFoundException if post not found
     */
    public List<Comment> getCommentsByPostId(Long id) throws PostNotFoundException {
        postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("id-" + id));
        return commentRepository.findAllCommentsByPostId(id);
    }
    /**
     * Get all comments by user id
     * @param id User id
     * @return List of comments
     * @throws UserNotFoundException if user not found
     */
    public List<Comment> getCommentsByUserId(Long id) throws UserNotFoundException {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("id-" + id));
        return commentRepository.findAllCommentsByAuthorId(id);
    }
    /**
     * Get comment by id and user id
     * @param id Comment id
     * @param userId User id
     * @return Comment
     * @throws CommentNotFoundForUserException if comment not found for user
     * @throws UserNotFoundException if user not found
     */
    public Comment getCommentByIdAndUserId(Long id, Long userId) throws CommentNotFoundForUserException, UserNotFoundException {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("id-" + userId));
        return commentRepository.findCommentByIdAndAuthorId(id, userId).orElseThrow(() -> new CommentNotFoundForUserException("id-" + id + " and userId-" + userId));
    }
    /**
     * Get comment by id and post id
     * @param id Comment id
     * @param postId Post id
     * @return Comment
     * @throws CommentNotFoundForPostException if comment not found for post
     * @throws PostNotFoundException if post not found
     */
    public Comment getCommentByIdAndPostId(Long id, Long postId) throws PostNotFoundException, CommentNotFoundForPostException {
        postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("id-" + postId));
        return commentRepository.findCommentByIdAndPostId(id, postId).orElseThrow(() -> new CommentNotFoundForPostException("id-" + id + " and postId-" + postId));
    }
    /**
     * Remove comment by id and user id
     * @param id Comment id
     * @param userId User id
     * @throws CommentNotFoundForUserException if comment not found for user
     * @throws UserNotFoundException if user not found
     */
    public void removeCommentByIdAndUserId(Long id, Long userId) throws CommentNotFoundForUserException, UserNotFoundException {
        getCommentByIdAndUserId(id, userId);
        commentRepository.deleteCommentByIdAndAuthorId(id, userId);
    }




}
