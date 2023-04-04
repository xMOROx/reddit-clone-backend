package com.example.backendclonereddit.controllers;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.Comment;
import com.example.backendclonereddit.models.CommentModel;
import com.example.backendclonereddit.services.CommentService;
import com.example.backendclonereddit.utils.exceptions.CommentNotFoundException;
import com.example.backendclonereddit.utils.models.assemblers.CommentModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = ApiPaths.CorsOriginLink.LINK)
@RequestMapping(path = ApiPaths.CommentCtrl.CTRL)
public class CommentController {
    private final CommentService commentService;
    private final CommentModelAssembler commentModelAssembler;

    public CommentController(CommentService commentService, CommentModelAssembler commentModelAssembler) {
        this.commentService = commentService;
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

//    TODO: Create adding/deleting/updating comment to post functionality per user
}
