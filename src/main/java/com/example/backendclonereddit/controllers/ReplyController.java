package com.example.backendclonereddit.controllers;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.Reply;
import com.example.backendclonereddit.models.ReplyModel;
import com.example.backendclonereddit.services.CommentService;
import com.example.backendclonereddit.services.PostService;
import com.example.backendclonereddit.services.ReplyService;
import com.example.backendclonereddit.services.UserService;
import com.example.backendclonereddit.utils.exceptions.types.CommentNotFoundException;
import com.example.backendclonereddit.utils.exceptions.types.ReplyNotFoundException;
import com.example.backendclonereddit.utils.exceptions.types.UserNotFoundException;
import com.example.backendclonereddit.utils.models.assemblers.ReplyModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
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
    @GetMapping("")
    public ResponseEntity<CollectionModel<ReplyModel>> getAllReplies() {
        List<Reply> replies = replyService.getAllReplies();
        return new ResponseEntity<>(
                replyModelAssembler.toCollectionModel(replies),
                HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ReplyModel> getReplyById(@PathVariable Long id) throws ReplyNotFoundException {
        return Stream.of(replyService.getReplyById(id))
                .map(replyModelAssembler::toModel)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ReplyNotFoundException("Reply not found with id: " + id));
    }


    @PostMapping("")
    public ResponseEntity<Reply> createReply(@RequestBody Reply reply, @RequestParam Long userId, @RequestParam Long commentId) throws CommentNotFoundException, UserNotFoundException {
        reply.setUser(userService.getUserById(userId));
        reply.setParentComment(commentService.getCommentById(commentId));

        replyService.createNewReply(reply);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reply.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }


}
