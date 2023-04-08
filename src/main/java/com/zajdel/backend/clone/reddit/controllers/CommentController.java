package com.zajdel.backend.clone.reddit.controllers;

import com.zajdel.backend.clone.reddit.configs.ApiPaths;
import com.zajdel.backend.clone.reddit.entities.Comment;
import com.zajdel.backend.clone.reddit.models.CommentModel;
import com.zajdel.backend.clone.reddit.services.CommentService;
import com.zajdel.backend.clone.reddit.services.PostService;
import com.zajdel.backend.clone.reddit.services.UserService;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.CommentNotFoundException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.PostNotFoundException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.UserNotFoundException;
import com.zajdel.backend.clone.reddit.utils.models.assemblers.CommentModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@CrossOrigin()
@RequestMapping(path = ApiPaths.CommentCtrl.CTRL)
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;
    private final CommentModelAssembler commentModelAssembler;

    public CommentController(CommentService commentService, UserService userService, PostService postService, CommentModelAssembler commentModelAssembler) {
        this.commentService = commentService;
        this.userService = userService;
        this.postService = postService;
        this.commentModelAssembler = commentModelAssembler;

    }

    /**
     * Get all comments
     *
     * @return List of comments and Response ok
     */
    @GetMapping(path = "")
    public ResponseEntity<CollectionModel<CommentModel>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        return new ResponseEntity<>(
                commentModelAssembler.toCollectionModel(comments), HttpStatus.OK);
    }

    /**
     * Get comment by id
     *
     * @param id Comment id
     * @return Response ok
     * @throws CommentNotFoundException if comment not found
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<CommentModel> getCommentById(@PathVariable Long id) throws CommentNotFoundException {
        var comment = commentService.getCommentById(id);
        return Stream.of(comment)
                .map(commentModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .findFirst()
                .orElseThrow(() -> new CommentNotFoundException("id-" + id));
    }

    /**
     * Delete comment by id
     *
     * @param id Comment id
     * @return Response no content
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Comment> deleteCommentById(@PathVariable Long id) {
        commentService.remove(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Create comment for post by id and user by id
     *
     * @param postId request param Post id
     * @param userId request param User id
     * @return List of comments and Response ok
     * @throws PostNotFoundException if post not found
     * @throws UserNotFoundException if user not found
     */
    @PostMapping(path = "")
    public ResponseEntity<Comment> createCommentForPostPerUser(@RequestParam Long postId, @RequestParam Long userId, @RequestBody Comment comment) throws UserNotFoundException, PostNotFoundException {
        var user = userService.getUserById(userId);
        var post = postService.getPostById(postId);

        comment.setAuthor(user);
        comment.setPost(post);

        var savedComment = commentService.createNewComment(comment);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedComment.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Update comment for post by id and user by id - Full update or Create new comment if not exist
     *
     * @param postId    Post id as request param
     * @param userId    User id as request param
     * @param comment   Comment
     * @param commentId Comment id as path variable
     * @return Response no content if success
     * @throws UserNotFoundException if user not found
     * @throws PostNotFoundException if post not found
     */
    @PutMapping(path = "/{commentId}")
    public ResponseEntity<Comment> updateOrCreateCommentForPostPerUser(@RequestParam Long postId, @RequestParam Long userId, @RequestBody Comment comment, @PathVariable Long commentId) throws UserNotFoundException, PostNotFoundException {
        var user = userService.getUserById(userId);
        var post = postService.getPostById(postId);

        comment.setAuthor(user);
        comment.setPost(post);

        var updatedId = commentService.fullUpdate(commentId, comment);

        if (Objects.equals(updatedId, commentId)) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updatedId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Update comment for post by id and user by id -  Partial update - only the fields that are present in the request body will be updated. Does not override the other fields and does not create a new comment if it does not exist
     *
     * @param postId    Post id as request param
     * @param userId    User id as request param
     * @param comment   Comment
     * @param commentId Comment id as path variable
     * @return Response no content if success
     * @throws UserNotFoundException if user not found
     * @throws PostNotFoundException if post not found
     */
    @PatchMapping(path = "/{commentId}")
    public ResponseEntity<Comment> updateCommentForPostPerUser(@RequestParam Long postId, @RequestParam Long userId, @RequestBody Comment comment, @PathVariable Long commentId) throws UserNotFoundException, PostNotFoundException {
        var user = userService.getUserById(userId);
        var post = postService.getPostById(postId);

        comment.setAuthor(user);
        comment.setPost(post);

        var updatedId = commentService.partialUpdate(commentId, comment);

        return ResponseEntity.noContent().build();
    }


}
