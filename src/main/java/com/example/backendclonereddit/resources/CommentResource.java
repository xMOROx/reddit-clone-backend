package com.example.backendclonereddit.resources;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.Comment;
import com.example.backendclonereddit.models.CommentModel;
import com.example.backendclonereddit.repositories.CommentRepository;
import com.example.backendclonereddit.utils.exceptions.CommentNotFoundException;
import com.example.backendclonereddit.utils.models.assemblers.CommentModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

   @GetMapping(path = "")
    public ResponseEntity<CollectionModel<CommentModel>> getAllComments() {
            List<Comment> comments = commentRepository.findAll();
            return new ResponseEntity<>(
                    commentModelAssembler.toCollectionModel(comments), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CommentModel> getCommentById(@PathVariable Long id) {
        return commentRepository.findById(id)
                .map(commentModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CommentNotFoundException("id-" + id));
    }


}
