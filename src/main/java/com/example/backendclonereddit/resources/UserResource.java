package com.example.backendclonereddit.resources;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.Comment;
import com.example.backendclonereddit.entities.Post;
import com.example.backendclonereddit.entities.User;
import com.example.backendclonereddit.models.CommentModel;
import com.example.backendclonereddit.models.PostModel;
import com.example.backendclonereddit.models.UserModel;
import com.example.backendclonereddit.repositories.CommentRepository;
import com.example.backendclonereddit.repositories.PostRepository;
import com.example.backendclonereddit.repositories.UserRepository;
import com.example.backendclonereddit.utils.exceptions.CommentNotFoundException;
import com.example.backendclonereddit.utils.exceptions.PostNotFoundException;
import com.example.backendclonereddit.utils.exceptions.UserNotFoundException;
import com.example.backendclonereddit.utils.models.assemblers.CommentModelAssembler;
import com.example.backendclonereddit.utils.models.assemblers.PostModelAssembler;
import com.example.backendclonereddit.utils.models.assemblers.UserModelAssembler;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping(path = ApiPaths.UserCtrl.CTRL)
public class UserResource {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final UserModelAssembler userModelAssembler;
    private final PostModelAssembler postModelAssembler;
    private final CommentModelAssembler commentModelAssembler;


    public UserResource(UserRepository userRepository
            , PostRepository postRepository
            , CommentRepository commentRepository
            , UserModelAssembler userModelAssembler
            , PostModelAssembler postModelAssembler
            , CommentModelAssembler commentModelAssembler) {

        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userModelAssembler = userModelAssembler;
        this.postModelAssembler = postModelAssembler;
        this.commentModelAssembler = commentModelAssembler;
    }

//    ----------------- GET ALL USERS -----------------

    /**
     * Get all users
     * @return CollectionModel of UserModel wrapped in ResponseEntity
     */
    @GetMapping(path = "")
    public ResponseEntity<CollectionModel<UserModel>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(
                userModelAssembler.toCollectionModel(users),
                HttpStatus.OK);
    }

    /**
     * Get all posts by user id
     * @param id as a PathVariable
     * @return CollectionModel of PostModel wrapped in ResponseEntity
     * @throws UserNotFoundException if the user does not exist
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long id) throws UserNotFoundException {

        return userRepository.findById(id)
                .map(userModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException("id-" + id));

    }

    /**
     * Get all posts by user id
     * @param user as a RequestBody
     * @return CollectionModel of PostModel wrapped in ResponseEntity
     */
    @PostMapping(path ="")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser =  userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     *  Checks if a user exists
     * @param id as a PathVariable
     * @throws UserNotFoundException if the user does not exist

     */
    @DeleteMapping(path = "/{id}")
    public void deleteUserById(@PathVariable Long id) throws UserNotFoundException {
        this.checkUserExists(id);

        userRepository.deleteById(id);
    }

    /**
     *  Updates a user with the given id using PUT method
     * @param id as a PathVariable
     * @param user as a RequestBody
     * @return User wrapped in ResponseEntity
     * @throws UserNotFoundException if the user does not exist
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) throws UserNotFoundException {
        this.checkUserExists(id);

        user.setId(id);

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

//    ----------------- USER POSTS -----------------

    /**
     *  Get all posts by user id
     * @param id as a PathVariable
     * @return CollectionModel of PostModel wrapped in ResponseEntity
     * @throws UserNotFoundException if the user does not exist
     */
    @GetMapping(path = "/{id}/posts")
    public ResponseEntity<CollectionModel<PostModel>> getAllPostsByUserId(@PathVariable Long id) throws UserNotFoundException {
        var user = this.checkUserExists(id);

        List<Post> posts = user.get().getPosts();

        return new ResponseEntity<>(
                postModelAssembler.toCollectionModel(posts),
                HttpStatus.OK);
    }

    /**
     *  Get user post using user id and post id
     * @param id as a PathVariable
     * @param postId as a PathVariable
     * @return PostModel wrapped in ResponseEntity
     * @throws UserNotFoundException if the user does not exist
     * @throws PostNotFoundException if the post does not exist
     */
    @GetMapping(path = "/{id}/posts/{postId}")
    public ResponseEntity<PostModel> getPostById(@PathVariable Long id, @PathVariable Long postId) throws UserNotFoundException, PostNotFoundException {
        this.checkUserExists(id);

        var post = this.checkPostExists(postId);

        return post
                .map(postModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PostNotFoundException("id-" + postId + " for user id-" + id));
    }

    /**
     * Creates a post for a user
     * @param id user id
     * @param post post to be created
     * @return the created post wrapped in ResponseEntity
     * @throws UserNotFoundException if the user does not exist
     *
     */
    @PostMapping(path = "/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable Long id, @Valid @RequestBody Post post) throws UserNotFoundException {
        var user = this.checkUserExists(id);

        post.setUser(user.get());

        Post savedPost =  postRepository.save(post);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Delete a post by user id and post id
     * @param id user id
     * @param postId post id
     * @throws UserNotFoundException if the user does not exist
     * @throws PostNotFoundException if the post does not exist
     */
    @DeleteMapping(path = "/{id}/posts/{postId}")
    public void deleteUserPost(@PathVariable Long id, @PathVariable Long postId) throws UserNotFoundException, PostNotFoundException {
        this.checkUserExists(id);
        this.checkPostExists(postId);

        postRepository.deleteById(postId);
    }


//    ----------------- USER COMMENTS -----------------

    /**
     * Get all comments by user id
     * @param id as a PathVariable
     * @return List of comments
     * @throws UserNotFoundException if the user does not exist
     */
    @GetMapping(path = "/{id}/comments")
    public ResponseEntity<CollectionModel<CommentModel>> getAllCommentsByUserId(@PathVariable Long id) throws UserNotFoundException {
        var user = this.checkUserExists(id);

        List<Comment> comments = user.get().getComments();

        return new ResponseEntity<>(
                commentModelAssembler.toCollectionModel(comments),
                HttpStatus.OK);
    }

    /**
     * Get a comment by user id and comment id
     * @param id as a PathVariable
     * @param commentId as a PathVariable
     * @return CommentModel wrapped in ResponseEntity
     * @throws UserNotFoundException if the user does not exist
     * @throws CommentNotFoundException if the comment does not exist
     */
    @GetMapping(path = "/{id}/comments/{commentId}")
    public ResponseEntity<CommentModel> getCommentById(@PathVariable Long id, @PathVariable Long commentId) throws UserNotFoundException, CommentNotFoundException {
        this.checkUserExists(id);

        var comment = this.checkCommentExists(commentId);

        return comment
                .map(commentModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CommentNotFoundException("id-" + commentId + " for user id-" + id));
    }

    /**
     * Creates a comment for a user
     * @param id user id
     * @param comment comment to be created
     * @return the created comment wrapped in ResponseEntity
     * @throws UserNotFoundException if the user does not exist
     */
    @PostMapping(path = "/{id}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable Long id, @Valid @RequestBody Comment comment) throws UserNotFoundException {
        var user = this.checkUserExists(id);

        comment.setUser(user.get());

        Comment savedComment =  commentRepository.save(comment);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedComment.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Delete a comment by user id and comment id
     * @param id user id
     * @param commentId comment id
     * @throws UserNotFoundException if the user does not exist
     * @throws CommentNotFoundException if the comment does not exist
     */
    @DeleteMapping(path = "/{id}/comments/{commentId}")
    public void deleteUserComment(@PathVariable Long id, @PathVariable Long commentId) throws UserNotFoundException, CommentNotFoundException {
        this.checkUserExists(id);
        this.checkCommentExists(commentId);

        commentRepository.deleteById(commentId);
    }


    //    ----------------- HELPER METHODS -----------------
    /**
     * Check if a user exists with given id
      * @param id as Long
     * @return Optional of User
     * @throws UserNotFoundException if the user does not exist
     */
    private @NotNull Optional<User> checkUserExists(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("id-" + id);
        }

        return user;
    }

    /**
     * Check if a post exists with given id
     * @param id as Long
     * @return Optional of Post
     * @throws PostNotFoundException if the post does not exist
     */
    private @NotNull Optional<Post> checkPostExists(Long id) throws PostNotFoundException {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new PostNotFoundException("id-" + id);
        }

        return post;
    }

    /**
     *  Check if a comment exists with given id
     * @param commentId as Long
     * @return Optional of Comment
     * @throws CommentNotFoundException if the comment does not exist
     */
    private @NotNull Optional<Comment> checkCommentExists(Long commentId) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isEmpty()) {
            throw new CommentNotFoundException("id-" + commentId);
        }
        return comment;
    }
}
