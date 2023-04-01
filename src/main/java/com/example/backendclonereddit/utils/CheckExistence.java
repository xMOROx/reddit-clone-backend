package com.example.backendclonereddit.utils;

import com.example.backendclonereddit.entities.Comment;
import com.example.backendclonereddit.entities.Post;
import com.example.backendclonereddit.entities.User;
import com.example.backendclonereddit.repositories.CommentRepository;
import com.example.backendclonereddit.repositories.PostRepository;
import com.example.backendclonereddit.repositories.UserRepository;
import com.example.backendclonereddit.utils.exceptions.CommentNotFoundException;
import com.example.backendclonereddit.utils.exceptions.PostNotFoundException;
import com.example.backendclonereddit.utils.exceptions.UserNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CheckExistence {

    //    ----------------- HELPER METHODS -----------------

    /**
     * Check if a user exists with given id
     * @param id as Long
     * @return Optional of User
     * @throws UserNotFoundException if the user does not exist
     */
    public static @NotNull Optional<User> checkUserExists(Long id, UserRepository userRepository) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("id-" + id);
        }

        return user;
    }

    /**
     * Check if a post exists with given id
     * @param id as Long
     * @return Optional of Post
     * @throws PostNotFoundException if the post does not exist
     */
    public static @NotNull Optional<Post> checkPostExists(Long id, PostRepository postRepository) throws PostNotFoundException {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new PostNotFoundException("id-" + id);
        }

        return post;
    }

    /**
     *  Check if a comment exists with given id
     * @param commentId as Long
     * @return Optional of Comment
     * @throws CommentNotFoundException if the comment does not exist
     */
    public static @NotNull Optional<Comment> checkCommentExists(Long commentId, CommentRepository commentRepository) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isEmpty()) {
            throw new CommentNotFoundException("id-" + commentId);
        }
        return comment;
    }
}
