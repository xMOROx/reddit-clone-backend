package com.example.backendclonereddit.controllers;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.Comment;
import com.example.backendclonereddit.models.CommentModel;
import com.example.backendclonereddit.services.CommentService;
import com.example.backendclonereddit.services.PostService;
import com.example.backendclonereddit.services.UserService;
import com.example.backendclonereddit.utils.exceptions.CommentNotFoundException;
import com.example.backendclonereddit.utils.exceptions.PostNotFoundException;
import com.example.backendclonereddit.utils.exceptions.UserNotFoundException;
import com.example.backendclonereddit.utils.models.assemblers.CommentModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = ApiPaths.CorsOriginLink.LINK)
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
    * @param id Comment id
    * @return Response ok
    * @throws CommentNotFoundException if comment not found
    */
   @GetMapping(path = "/{id}")
   public ResponseEntity<CommentModel> getCommentById(@PathVariable Long id) throws CommentNotFoundException {
       var comment = commentService.getCommentById(id);
       return  Stream.of(comment)
                .map(commentModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .findFirst()
                .orElseThrow(() -> new CommentNotFoundException("id-" + id));
   }

    /**
     * Delete comment by id
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
     * @param postId Post id
     * @return List of comments and Response ok
     * @throws PostNotFoundException if post not found
     * @throws UserNotFoundException if user not found
     */
    @PostMapping(path = "/posts/{postId}/users/{userId}")
    public ResponseEntity<Comment> createCommentForPost(@PathVariable Long postId, @PathVariable Long userId, @RequestBody Comment comment) throws UserNotFoundException, PostNotFoundException {
        var user = userService.getUserById(userId);
        var post = postService.getPostById(postId);

        comment.setUser(user);
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
     * Update comment for post by id and user by id
     * @param postId Post id
     * @param userId  User id
     * @param comment Comment
     * @param commentId Comment id
     * @return List of comments and Response ok
     * @throws PostNotFoundException if post not found
     * @throws UserNotFoundException if user not found
     */

    @PutMapping(path = "{commentId}/posts/{postId}/users/{userId}")
    public ResponseEntity<Comment> updateCommentForPost(@PathVariable Long postId, @PathVariable Long userId, @RequestBody Comment comment, @PathVariable Long commentId) throws UserNotFoundException, PostNotFoundException {
        var user = userService.getUserById(userId);
        var post = postService.getPostById(postId);

        comment.setUser(user);
        comment.setPost(post);

        var updatedId = commentService.fullUpdate(commentId, comment);

        if(Objects.equals(updatedId, commentId)) {
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
     * Update comment for post by id and user by id - Partial update
     * @param postId Post id
     * @param userId User id
     * @param comment Comment
     * @param commentId Comment id
     * @return Response no content if success
     * @throws UserNotFoundException if user not found
     * @throws PostNotFoundException if post not found
     */
    @PatchMapping(path = "{commentId}/posts/{postId}/users/{userId}")
    public ResponseEntity<Comment> updatePartialCommentForPost(@PathVariable Long postId, @PathVariable Long userId, @RequestBody Comment comment, @PathVariable Long commentId) throws UserNotFoundException, PostNotFoundException {
        var user = userService.getUserById(userId);
        var post = postService.getPostById(postId);

        comment.setUser(user);
        comment.setPost(post);

        var updatedId = commentService.partialUpdate(commentId, comment);

        return ResponseEntity.noContent().build();
    }


}
