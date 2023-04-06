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


    @PostMapping(path = "/posts/{postId}/users/{userId}")
    public ResponseEntity<CommentModel> createCommentForPost(@PathVariable Long postId, @PathVariable Long userId, @RequestBody Comment comment) throws UserNotFoundException, PostNotFoundException {
        var user = userService.getUserById(userId);
        var post = postService.getPostById(postId);

        comment.setUser(user);
        comment.setPost(post);

        var savedComment = commentService.createNewComment(comment);

        return Stream.of(savedComment)
                .map(commentModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .findFirst()
                .orElseThrow(() -> new CommentNotFoundException("id-" + comment.getId()));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CommentModel> deleteCommentById(@PathVariable Long id) throws CommentNotFoundException {
        commentService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/posts/{postId}/users/{userId}/comments/{commentId}")
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

    @PatchMapping(path = "/posts/{postId}/users/{userId}/comments/{commentId}")
    public ResponseEntity<Comment> updatePartialCommentForPost(@PathVariable Long postId, @PathVariable Long userId, @RequestBody Comment comment, @PathVariable Long commentId) throws UserNotFoundException, PostNotFoundException {
        var user = userService.getUserById(userId);
        var post = postService.getPostById(postId);

        comment.setUser(user);
        comment.setPost(post);

        var updatedId = commentService.partialUpdate(commentId, comment);

        return ResponseEntity.noContent().build();
    }


}
