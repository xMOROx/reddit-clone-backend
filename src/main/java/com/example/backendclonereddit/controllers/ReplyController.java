package com.example.backendclonereddit.controllers;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.Reply;
import com.example.backendclonereddit.models.ReplyModel;
import com.example.backendclonereddit.services.CommentService;
import com.example.backendclonereddit.services.ReplyService;
import com.example.backendclonereddit.services.UserService;
import com.example.backendclonereddit.utils.exceptions.types.CommentNotFoundException;
import com.example.backendclonereddit.utils.exceptions.types.ReplyNotFoundException;
import com.example.backendclonereddit.utils.exceptions.types.UserNotFoundException;
import com.example.backendclonereddit.utils.models.assemblers.ReplyModelAssembler;
import org.jetbrains.annotations.NotNull;
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
@RequestMapping(ApiPaths.ReplyCtrl.CTRL)
public class ReplyController {
    private final ReplyService replyService;

    private final UserService userService;

    private final CommentService commentService;

    private final ReplyModelAssembler replyModelAssembler;

    public ReplyController(ReplyService replyService, UserService userService, CommentService commentService, ReplyModelAssembler replyModelAssembler) {
        this.replyService = replyService;
        this.userService = userService;
        this.commentService = commentService;
        this.replyModelAssembler = replyModelAssembler;
    }

    /**
     * Get all replies
     * @return List of replies and Response ok
     */
    @GetMapping("")
    public ResponseEntity<CollectionModel<ReplyModel>> getAllReplies() {
        List<Reply> replies = replyService.getAllReplies();
        return new ResponseEntity<>(
                replyModelAssembler.toCollectionModel(replies),
                HttpStatus.OK);
    }

    /**
     * Get reply by id
     * @param id Reply id
     * @return Response ok
     * @throws ReplyNotFoundException if reply not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReplyModel> getReplyById(@PathVariable Long id) throws ReplyNotFoundException {
        return Stream.of(replyService.getReplyById(id))
                .map(replyModelAssembler::toModel)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ReplyNotFoundException("Reply not found with id: " + id));
    }

    /**
     * Create new reply
     * @param reply Reply object
     * @param userId User id
     * @param commentId Comment id
     * @return Response created
     * @throws CommentNotFoundException if comment not found
     * @throws UserNotFoundException if user not found
     */
    @PostMapping("")
    public ResponseEntity<Reply> createReply(@RequestBody @NotNull Reply reply, @RequestParam Long userId, @RequestParam Long commentId) throws CommentNotFoundException, UserNotFoundException {
        var user = userService.getUserById(userId);
        var comment = commentService.getCommentById(commentId);

        reply.setAuthor(user);
        reply.setParentComment(comment);

        replyService.createNewReply(reply);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reply.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Update or create reply by id - full update. If reply not found, create new reply.
     * @param id Reply id
     * @param reply Reply object
     * @param commentId Comment id
     * @param userId User id
     * @return Response created
     * @throws CommentNotFoundException if comment not found
     * @throws UserNotFoundException if user not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Reply> updateReply(@PathVariable Long id, @RequestBody @NotNull Reply reply, @RequestParam Long commentId, @RequestParam Long userId) throws CommentNotFoundException, UserNotFoundException {
        var user = userService.getUserById(userId);
        var comment = commentService.getCommentById(commentId);

        reply.setAuthor(user);
        reply.setParentComment(comment);

        var updatedReplyId = replyService.fullUpdate(id, reply);
        if (Objects.equals(updatedReplyId, id)) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updatedReplyId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Update a reply by id - partial update. If reply not found, throw ReplyNotFoundException.
     * @param id Reply id
     * @param reply Reply object
     * @param commentId Comment id
     * @param userId User id
     * @return Response created
     * @throws ReplyNotFoundException if reply not found
     * @throws CommentNotFoundException if comment not found
     * @throws UserNotFoundException if user not found
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Reply> patchReply(@PathVariable Long id, @RequestBody @NotNull Reply reply, @RequestParam Long commentId, @RequestParam Long userId) throws ReplyNotFoundException, CommentNotFoundException, UserNotFoundException {
        var user = userService.getUserById(userId);
        var comment = commentService.getCommentById(commentId);

        reply.setAuthor(user);
        reply.setParentComment(comment);

        var updatedReplyId = replyService.partialUpdate(id, reply);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete reply by id
     * @param id Reply id
     * @return Response no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Reply> deleteReply(@PathVariable Long id) {
        replyService.remove(id);
        return ResponseEntity.noContent().build();
    }




}
