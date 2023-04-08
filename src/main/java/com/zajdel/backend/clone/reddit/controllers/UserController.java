package com.example.backendclonereddit.controllers;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.Comment;
import com.example.backendclonereddit.entities.Post;
import com.example.backendclonereddit.entities.SubReddit;
import com.example.backendclonereddit.entities.User;
import com.example.backendclonereddit.models.CommentModel;
import com.example.backendclonereddit.models.PostModel;
import com.example.backendclonereddit.models.SubRedditModel;
import com.example.backendclonereddit.models.UserModel;
import com.example.backendclonereddit.services.CommentService;
import com.example.backendclonereddit.services.PostService;
import com.example.backendclonereddit.services.SubRedditService;
import com.example.backendclonereddit.services.UserService;
import com.example.backendclonereddit.utils.exceptions.types.*;
import com.example.backendclonereddit.utils.models.assemblers.CommentModelAssembler;
import com.example.backendclonereddit.utils.models.assemblers.PostModelAssembler;
import com.example.backendclonereddit.utils.models.assemblers.SubRedditModelAssembler;
import com.example.backendclonereddit.utils.models.assemblers.UserModelAssembler;
import jakarta.validation.Valid;
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
@CrossOrigin()
@RequestMapping(path = ApiPaths.UserCtrl.CTRL)
public class UserController {

    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;
    private final SubRedditService subRedditService;

    private final UserModelAssembler userModelAssembler;
    private final PostModelAssembler postModelAssembler;
    private final CommentModelAssembler commentModelAssembler;
    private final SubRedditModelAssembler subRedditModelAssembler;

    public UserController(UserService userService, PostService postService, CommentService commentService, SubRedditService subRedditService, UserModelAssembler userModelAssembler, PostModelAssembler postModelAssembler, CommentModelAssembler commentModelAssembler, SubRedditModelAssembler subRedditModelAssembler) {

        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.subRedditService = subRedditService;

        this.userModelAssembler = userModelAssembler;
        this.postModelAssembler = postModelAssembler;
        this.commentModelAssembler = commentModelAssembler;
        this.subRedditModelAssembler = subRedditModelAssembler;
    }

//    ----------------- GET ALL USERS -----------------

    /**
     * Get all users using GET method
     *
     * @return Response ok
     */
    @GetMapping(path = "")
    public ResponseEntity<CollectionModel<UserModel>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        return new ResponseEntity<>(userModelAssembler.toCollectionModel(users), HttpStatus.OK);
    }

    /**
     * Get user by id using GET method
     *
     * @param id as a PathVariable
     * @return Response ok
     * @throws UserNotFoundException if the user does not exist
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long id) throws UserNotFoundException {

        return Stream.of(userService.getUserById(id)).map(userModelAssembler::toModel).map(ResponseEntity::ok).findFirst().orElseThrow(() -> new UserNotFoundException("id-" + id));

    }

    /**
     * Creates a new user using POST method
     *
     * @param user as a RequestBody
     * @return Response created with the URI location of the new user
     */
    @PostMapping(path = "")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userService.createNewUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Deletes a user with the given id using DELETE method
     *
     * @param id as a PathVariable
     * @return Response no content
     * @throws UserNotFoundException if the user does not exist
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable Long id) throws UserNotFoundException {
        userService.remove(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates or Creates a user with the given id using PUT method - full update or create if not exists
     *
     * @param id   as a PathVariable
     * @param user as a RequestBody
     * @return Response created with the URI location of the new user or no content if the user was updated
     * @throws UserNotFoundException if the user does not exist
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity<User> updateOrCreateUser(@PathVariable Long id, @Valid @RequestBody User user) throws UserNotFoundException {
        Long updatedId = userService.fullUpdate(id, user);

        if (Objects.equals(updatedId, id)) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updatedId).toUri();

        return ResponseEntity.created(location).build();

    }

    /**
     * Updates a user with the given id using PATCH method -  Partial update - only the fields that are present in the request body will be updated. Does not override the other fields and does not create a new comment if it does not exist
     *
     * @param id   as a PathVariable
     * @param user as a RequestBody
     * @return Response created with the URI location of the new user
     * @throws UserNotFoundException if the user does not exist
     */

    @PatchMapping(path = "/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) throws UserNotFoundException {
        Long updatedId = userService.partialUpdate(id, user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updatedId).toUri();

        return ResponseEntity.created(location).build();

    }

//    ----------------- USER POSTS -----------------

    /**
     * Get all posts by user id using GET method
     *
     * @param id as a PathVariable
     * @return Response ok
     * @throws UserNotFoundException if the user does not exist
     */
    @GetMapping(path = "/{id}/posts")
    public ResponseEntity<CollectionModel<PostModel>> getAllPostsByUserId(@PathVariable Long id) throws UserNotFoundException {
        List<Post> posts = postService.getPostsByUserId(id);
        System.out.println("-----------------------------------------------------------");
        System.out.println("Posts: ");
        System.out.println(posts);
        System.out.println("-----------------------------------------------------------");

        return new ResponseEntity<>(postModelAssembler.toCollectionModel(posts), HttpStatus.OK);
    }

    /**
     * Get user post using user id and post id using GET method
     *
     * @param id     as a PathVariable
     * @param postId as a PathVariable
     * @return Response ok
     * @throws PostNotFoundForUserException if the post does not exist for the user
     * @throws UserNotFoundException        if the user does not exist
     */
    @GetMapping(path = "/{id}/posts/{postId}")
    public ResponseEntity<PostModel> getPostById(@PathVariable Long id, @PathVariable Long postId) throws PostNotFoundForUserException, UserNotFoundException {
        var user = userService.getUserById(id);
        var post = postService.getPostByIdForUserId(postId, id);

        return Stream.of(post).map(postModelAssembler::toModel).map(ResponseEntity::ok).findFirst().orElseThrow(() -> new PostNotFoundException("id-" + postId + " for user id-" + id));
    }

    /**
     * Creates a post for a user using POST method
     *
     * @param id   user id
     * @param post post to be created
     * @return Response created with the URI location of the new post for the user
     * @throws UserNotFoundException if the user does not exist
     */
    @PostMapping(path = "/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable Long id, @Valid @RequestBody Post post) throws UserNotFoundException {
        var user = userService.getUserById(id);

        post.setAuthor(user);

        Post savedPost = postService.createNewPost(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPost.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Update a post by user id and post id using PUT method - Full update or create. It will override the post if it exists
     *
     * @param id     user id
     * @param postId post id
     * @param post   post to be updated
     * @return ResponseEntity with no content or created
     * @throws UserNotFoundException if the user does not exist
     */
    @PutMapping(path = "/{id}/posts/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @PathVariable Long postId, @Valid @RequestBody Post post) throws UserNotFoundException {
        var user = userService.getUserById(id);

        post.setAuthor(user);

        Long updatedId = postService.fullUpdate(postId, post);

        if (Objects.equals(updatedId, postId)) {
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updatedId).toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Update a post by user id and post id using PATCH method - Partial update - only the fields that are present in the request body will be updated. Does not override the other fields and does not create a new comment if it does not exist
     *
     * @param id     user id
     * @param postId post id
     * @param post   post to be updated
     * @return ResponseEntity with no content
     * @throws UserNotFoundException if the user does not exist
     */
    @PatchMapping(path = "/{id}/posts/{postId}")
    public ResponseEntity<Post> partialUpdatePost(@PathVariable Long id, @PathVariable Long postId, @Valid @RequestBody Post post) throws UserNotFoundException {
        var user = userService.getUserById(id);

        post.setAuthor(user);

        Long updatedId = postService.partialUpdate(postId, post);

        return ResponseEntity.noContent().build();
    }

    /**
     * Delete a post by user id and post id using DELETE method
     *
     * @param id     user id
     * @param postId post id to be deleted
     * @return ResponseEntity with no content
     * @throws UserNotFoundException        if the user does not exist
     * @throws PostNotFoundForUserException if the post does not exist for the user
     */
    @DeleteMapping(path = "/{id}/posts/{postId}")
    public ResponseEntity<Post> deleteUserPost(@PathVariable Long id, @PathVariable Long postId) throws UserNotFoundException {

        var user = userService.getUserById(id); // check if user exists
        var post = postService.getPostByIdForUserId(postId, id); // check if post exists for user

        postService.removeByUserId(postId, id);
        return ResponseEntity.noContent().build();
    }


//    ----------------- USER COMMENTS -----------------

    /**
     * Get all comments by user id using GET method
     *
     * @param id as a PathVariable
     * @return CollectionModel of CommentModel wrapped in ResponseEntity
     * @throws UserNotFoundException if the user does not exist
     */
    @GetMapping(path = "/{id}/comments")
    public ResponseEntity<CollectionModel<CommentModel>> getAllCommentsByUserId(@PathVariable Long id) throws UserNotFoundException {
        var user = userService.getUserById(id);

        List<Comment> comments = commentService.getCommentsByUserId(id);

        return new ResponseEntity<>(commentModelAssembler.toCollectionModel(comments), HttpStatus.OK);
    }

    /**
     * Get a comment by user id and comment id using GET method
     *
     * @param id        as a PathVariable
     * @param commentId as a PathVariable
     * @return Response ok
     * @throws UserNotFoundException           if the user does not exist
     * @throws CommentNotFoundForUserException if the comment does not exist
     */
    @GetMapping(path = "/{id}/comments/{commentId}")
    public ResponseEntity<CommentModel> getCommentById(@PathVariable Long id, @PathVariable Long commentId) throws UserNotFoundException, CommentNotFoundForUserException {
        var user = userService.getUserById(id);
        var comment = commentService.getCommentByIdAndUserId(commentId, id);

        return Stream.of(comment).map(commentModelAssembler::toModel).map(ResponseEntity::ok).findFirst().orElseThrow(() -> new CommentNotFoundException("id-" + commentId + " for user id-" + id));
    }

    /**
     * Delete a comment by user id and comment id
     *
     * @param id        user id
     * @param commentId comment id
     * @return ResponseEntity with no content
     * @throws UserNotFoundException           if the user does not exist
     * @throws CommentNotFoundForUserException if the comment does not exist
     */
    @DeleteMapping(path = "/{id}/comments/{commentId}")
    public ResponseEntity<Comment> deleteUserComment(@PathVariable Long id, @PathVariable Long commentId) throws UserNotFoundException, CommentNotFoundForUserException {
        var user = userService.getUserById(id); // check if user exists
        var comment = commentService.getCommentByIdAndUserId(commentId, id); // check if comment exists for user

        commentService.removeByUserId(commentId, id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update a comment by user id and comment id using PATCH method - Partial update - only the fields that are present in the request body will be updated. Does not override the other fields and does not create a new comment if it does not exist
     *
     * @param id        user id
     * @param commentId comment id
     * @param comment   comment to be updated
     * @return ResponseEntity with no content
     * @throws UserNotFoundException           if the user does not exist
     * @throws CommentNotFoundForUserException if the comment does not exist
     */
    @PatchMapping(path = "/{id}/comments/{commentId}")
    public ResponseEntity<Comment> updateCommentForUser(@PathVariable Long id, @PathVariable Long commentId, @Valid @RequestBody Comment comment) throws UserNotFoundException, CommentNotFoundForUserException {
        var user = userService.getUserById(id);

        comment.setAuthor(user);

        Long updatedId = commentService.partialUpdate(commentId, comment);

        return ResponseEntity.noContent().build();
    }
//    ---------------------- SubReddit --------------------------------

    /**
     * Get all subreddits by user id using GET method
     *
     * @param id user id
     * @return CollectionModel of SubRedditModel wrapped in ResponseEntity
     * @throws UserNotFoundException if the user does not exist
     */

    @GetMapping(path = "/{id}/subreddits")
    public ResponseEntity<CollectionModel<SubRedditModel>> getAllSubRedditsByUserId(@PathVariable Long id) throws UserNotFoundException {
        var user = userService.getUserById(id);

        List<SubReddit> subReddits = subRedditService.getAllSubReddits();

        return new ResponseEntity<>(subRedditModelAssembler.toCollectionModel(subReddits), HttpStatus.OK);
    }

    /**
     * Create a new subreddit by user id using POST method
     *
     * @param id        user id
     * @param subReddit subreddit to be created
     * @return ResponseEntity with created status
     * @throws UserNotFoundException if the user does not exist
     */
    @PostMapping(path = "/{id}/subreddits")
    public ResponseEntity<SubReddit> createSubReddit(@PathVariable Long id, @Valid @RequestBody SubReddit subReddit) throws UserNotFoundException {
        var user = userService.getUserById(id);

        subReddit.setOwner(user);

        SubReddit savedSubReddit = subRedditService.createNewSubReddit(subReddit);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedSubReddit.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Create or update a subreddit by user id using PUT method - Full update - all fields must be present in the request body. If the subreddit does not exist, it will be created. If it exists, it will be updated
     *
     * @param id          user id
     * @param subRedditId subreddit id
     * @param subReddit   subreddit to be created or updated
     * @return ResponseEntity with created status
     * @throws UserNotFoundException      if the user does not exist
     * @throws SubRedditNotFoundException if the subreddit does not exist
     */
    @PutMapping(path = "/{id}/subreddits/{subRedditId}")
    public ResponseEntity<SubReddit> updateSubReddit(@PathVariable Long id, @PathVariable Long subRedditId, @Valid @RequestBody SubReddit subReddit) throws UserNotFoundException, SubRedditNotFoundException {
        var user = userService.getUserById(id);

        subReddit.setOwner(user);

        Long updatedId = subRedditService.fullUpdate(subRedditId, subReddit);
        if (Objects.equals(updatedId, subRedditId)) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(updatedId).toUri();

        return ResponseEntity.created(location).build();
    }

    /**
     * Update a subreddit by user id and subreddit id using PATCH method - Partial update - only the fields that are present in the request body will be updated. Does not override the other fields and does not create a new subreddit if it does not exist
     *
     * @param id          user id
     * @param subRedditId subreddit id
     * @param subReddit   subreddit to be updated
     * @return ResponseEntity with no content
     * @throws UserNotFoundException      if the user does not exist
     * @throws SubRedditNotFoundException if the subreddit does not exist
     */
    @PatchMapping(path = "/{id}/subreddits/{subRedditId}")
    public ResponseEntity<SubReddit> partialUpdateSubReddit(@PathVariable Long id, @PathVariable Long subRedditId, @Valid @RequestBody SubReddit subReddit) throws UserNotFoundException, SubRedditNotFoundException {
        var user = userService.getUserById(id);

        subReddit.setOwner(user);

        Long updatedId = subRedditService.partialUpdate(subRedditId, subReddit);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete a subreddit by user id and subreddit id using DELETE method
     *
     * @param id          user id
     * @param subRedditId subreddit id
     * @return ResponseEntity with no content
     * @throws UserNotFoundException      if the user does not exist
     * @throws SubRedditNotFoundException if the subreddit does not exist
     */
    @DeleteMapping(path = "/{id}/subreddits/{subRedditId}")
    public ResponseEntity<SubReddit> deleteSubReddit(@PathVariable Long id, @PathVariable Long subRedditId) throws UserNotFoundException, SubRedditNotFoundException {
        var user = userService.getUserById(id); // check if user exists
        var subReddit = subRedditService.getSubRedditById(subRedditId); // check if subReddit exists

        subRedditService.remove(subRedditId);
        return ResponseEntity.noContent().build();
    }

}
