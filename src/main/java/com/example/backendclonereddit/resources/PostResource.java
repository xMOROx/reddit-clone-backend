package com.example.backendclonereddit.resources;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.Comment;
import com.example.backendclonereddit.entities.Post;
import com.example.backendclonereddit.models.CommentModel;
import com.example.backendclonereddit.models.PostModel;
import com.example.backendclonereddit.repositories.CommentRepository;
import com.example.backendclonereddit.repositories.PostRepository;
import com.example.backendclonereddit.utils.CheckExistence;
import com.example.backendclonereddit.utils.exceptions.CommentNotFoundException;
import com.example.backendclonereddit.utils.exceptions.PostNotFoundException;
import com.example.backendclonereddit.utils.models.assemblers.CommentModelAssembler;
import com.example.backendclonereddit.utils.models.assemblers.PostModelAssembler;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(path = ApiPaths.PostCtrl.CTRL)
public class PostResource {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostModelAssembler postModelAssembler;
    private final CommentModelAssembler commentModelAssembler;


    public PostResource(PostRepository postRepository, CommentRepository commentRepository, PostModelAssembler postModelAssembler, CommentModelAssembler commentModelAssembler) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.postModelAssembler = postModelAssembler;
        this.commentModelAssembler = commentModelAssembler;

    }

    /**
     * Get all posts
     * @return List of posts
     */
    @GetMapping(path = "")
    public ResponseEntity<CollectionModel<PostModel>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return new ResponseEntity<>(
                postModelAssembler.toCollectionModel(posts), HttpStatus.OK);
    }

    /**
     * Get post by id
     * @param id Post id
     * @return  Post model wrapped in ResponseEntity
     * @throws PostNotFoundException if post not found
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<PostModel> getPostById(@PathVariable Long id) throws PostNotFoundException {
        Optional<Post> post = postRepository.findById(id);
        return post
                .map(PostModelAssembler::toPostModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException("id-" + id));
    }

//    ------------------ Comments ------------------

    /**
     * Get all comments
     * @param id Post id
     * @return List of comments
     * @throws PostNotFoundException if post not found
     * @throws CommentNotFoundException if comment not found
     */
    @GetMapping(path = "/{id}/comments")
    public ResponseEntity<CollectionModel<CommentModel>> getCommentsByPostId(@PathVariable Long id) throws PostNotFoundException, CommentNotFoundException {
        Optional<Post> post =  CheckExistence.checkPostExists(id, postRepository);

        List<Comment> comments = post.get().getComments();

        return new ResponseEntity<>(
                commentModelAssembler.toCollectionModel(comments), HttpStatus.OK);

    }

    /**
     * Create comment
     * @param id Post id
     * @return Comment model wrapped in ResponseEntity
     * @throws PostNotFoundException if post not found
     */
    @PostMapping(path = "/{id}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable Long id,@Valid @RequestBody Comment commentModel) throws PostNotFoundException {
        Optional<Post> post = CheckExistence.checkPostExists(id, postRepository);

        commentModel.setPost(post.get());

        Comment comment = commentRepository.save(commentModel);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(comment.getId())
                .toUri();


        return ResponseEntity.created(location).build();
    }

}
