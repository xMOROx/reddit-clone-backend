package com.example.backendclonereddit.resources;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.Comment;
import com.example.backendclonereddit.models.CommentModel;
import com.example.backendclonereddit.repositories.CommentRepository;
import com.example.backendclonereddit.utils.CheckExistence;
import com.example.backendclonereddit.utils.exceptions.CommentNotFoundException;
import com.example.backendclonereddit.utils.models.assemblers.CommentModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = ApiPaths.CommentCtrl.CTRL)
public class CommentResource {
    private final CommentRepository commentRepository;
    private final CommentModelAssembler commentModelAssembler;

    public CommentResource(CommentRepository commentRepository, CommentModelAssembler commentModelAssembler) {
        this.commentRepository = commentRepository;
        this.commentModelAssembler = commentModelAssembler;
    }
    /**
     * Get all comments
     * @return List of comments
     */
   @GetMapping(path = "")
   public ResponseEntity<CollectionModel<CommentModel>> getAllComments() {
            List<Comment> comments = commentRepository.findAll();
            return new ResponseEntity<>(
                    commentModelAssembler.toCollectionModel(comments), HttpStatus.OK);
   }
   /**
    * Get comment by id
    * @param id Comment id
    * @return Comment model wrapped in ResponseEntity
    * @throws CommentNotFoundException if comment not found
    */
   @GetMapping(path = "/{id}")
   public ResponseEntity<CommentModel> getCommentById(@PathVariable Long id) throws CommentNotFoundException {

       return commentRepository.findById(id)
                .map(commentModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CommentNotFoundException("id-" + id));
   }

   @PutMapping(path = "/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment comment) throws CommentNotFoundException {
        var commentToUpdate = CheckExistence.checkCommentExists(id, commentRepository).get();

    }

}
