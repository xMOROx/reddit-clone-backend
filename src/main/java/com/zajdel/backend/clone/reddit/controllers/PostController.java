package com.zajdel.backend.clone.reddit.controllers;

import com.zajdel.backend.clone.reddit.configs.ApiPaths;
import com.zajdel.backend.clone.reddit.entities.Comment;
import com.zajdel.backend.clone.reddit.entities.Post;
import com.zajdel.backend.clone.reddit.models.CommentModel;
import com.zajdel.backend.clone.reddit.models.PostModel;
import com.zajdel.backend.clone.reddit.services.CommentService;
import com.zajdel.backend.clone.reddit.services.PostService;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.PostNotFoundException;
import com.zajdel.backend.clone.reddit.utils.models.assemblers.CommentModelAssembler;
import com.zajdel.backend.clone.reddit.utils.models.assemblers.PostModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
@CrossOrigin()
@RequestMapping(path = ApiPaths.PostCtrl.CTRL)
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final PostModelAssembler postModelAssembler;
    private final CommentModelAssembler commentModelAssembler;


    public PostController(PostService postService, CommentService commentService, PostModelAssembler postModelAssembler, CommentModelAssembler commentModelAssembler) {
        this.postService = postService;
        this.commentService = commentService;
        this.postModelAssembler = postModelAssembler;
        this.commentModelAssembler = commentModelAssembler;

    }

    /**
     * Get all posts
     *
     * @return List of posts
     */
    @GetMapping(path = "")
    public ResponseEntity<CollectionModel<PostModel>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();

        return new ResponseEntity<>(
                postModelAssembler.toCollectionModel(posts), HttpStatus.OK);
    }

    /**
     * Get post by id
     *
     * @param id Post id
     * @return Response ok
     * @throws PostNotFoundException if post not found
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<PostModel> getPostById(@PathVariable Long id) throws PostNotFoundException {
        var post = postService.getPostById(id);
        return Stream.of(post)
                .map(PostModelAssembler::toPostModel)
                .map(ResponseEntity::ok)
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException("id-" + id));
    }

//    ------------------ Comments ------------------

    /**
     * Get all comments for post
     *
     * @param id Post id
     * @return List of comments with Response ok
     * @throws PostNotFoundException if post not found
     */
    @GetMapping(path = "/{id}/comments")
    public ResponseEntity<CollectionModel<CommentModel>> getCommentsByPostId(@PathVariable Long id) throws PostNotFoundException {
        var post = postService.getPostById(id);
        List<Comment> comments = commentService.getCommentsByPostId(id);

        return new ResponseEntity<>(
                commentModelAssembler.toCollectionModel(comments), HttpStatus.OK);

    }

}
