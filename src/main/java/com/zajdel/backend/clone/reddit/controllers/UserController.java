package com.zajdel.backend.clone.reddit.controllers;

import com.zajdel.backend.clone.reddit.configs.ApiPaths;
import com.zajdel.backend.clone.reddit.entities.Comment;
import com.zajdel.backend.clone.reddit.entities.Post;
import com.zajdel.backend.clone.reddit.entities.SubReddit;
import com.zajdel.backend.clone.reddit.entities.User;
import com.zajdel.backend.clone.reddit.models.CommentModel;
import com.zajdel.backend.clone.reddit.models.PostModel;
import com.zajdel.backend.clone.reddit.models.SubRedditModel;
import com.zajdel.backend.clone.reddit.models.UserModel;
import com.zajdel.backend.clone.reddit.services.CommentService;
import com.zajdel.backend.clone.reddit.services.PostService;
import com.zajdel.backend.clone.reddit.services.SubRedditService;
import com.zajdel.backend.clone.reddit.services.UserService;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.CommentNotFoundException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.CommentNotFoundForUserException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.PostNotFoundException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.PostNotFoundForUserException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.SubRedditNotFoundException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.UserNotFoundException;
import com.zajdel.backend.clone.reddit.utils.models.assemblers.CommentModelAssembler;
import com.zajdel.backend.clone.reddit.utils.models.assemblers.PostModelAssembler;
import com.zajdel.backend.clone.reddit.utils.models.assemblers.SubRedditModelAssembler;
import com.zajdel.backend.clone.reddit.utils.models.assemblers.UserModelAssembler;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    public UserController(UserService userService,
                          PostService postService,
                          CommentService commentService,
                          SubRedditService subRedditService,
                          UserModelAssembler userModelAssembler,
                          PostModelAssembler postModelAssembler,
                          CommentModelAssembler commentModelAssembler,
                          SubRedditModelAssembler subRedditModelAssembler) {

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

        return Stream.of(userService.getUserById(id)).map(userModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("id-" + id));

    }

    /**
     * Creates a new user using POST method
     *
     * @param user as a RequestBody
     * @return Response created with the URI location of the new user
     */
    @PostMapping(path = "")
    public ResponseEntity<Long> createUser(@Valid @RequestBody User user) {
        Long id = userService.createNewUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

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
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) throws UserNotFoundException {
        userService.removeUserById(id);
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
    public ResponseEntity<Long> updateOrCreateUser(@PathVariable Long id, @Valid @RequestBody User user) throws UserNotFoundException {
        Long updatedId = userService.fullUpdateUserById(id, user);

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
     * @return Response created with the URI location
     * @throws UserNotFoundException if the user does not exist
     */

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Long> updateUser(@PathVariable Long id, @Valid @RequestBody User user) throws UserNotFoundException {
        Long updatedId = userService.partialUpdateUserById(id, user);

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
        var post = postService.getPostByIdForUserById(postId, id);

        return Stream.of(post)
                .map(postModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException("id-" + postId + " for user id-" + id));
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
    public ResponseEntity<Long> createPost(@PathVariable Long id, @Valid @RequestBody Post post) throws UserNotFoundException {
        var user = userService.getUserById(id);

        post.setAuthor(user);

        Long postId = postService.createNewPost(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(postId).toUri();

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
    public ResponseEntity<Long> updatePost(@PathVariable Long id, @PathVariable Long postId, @Valid @RequestBody Post post) throws UserNotFoundException {
        var user = userService.getUserById(id);

        post.setAuthor(user);

        Long updatedId = postService.fullUpdatePostById(postId, post);

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
    public ResponseEntity<Long> partialUpdatePost(@PathVariable Long id, @PathVariable Long postId, @Valid @RequestBody Post post) throws UserNotFoundException {
        var user = userService.getUserById(id);

        post.setAuthor(user);

        Long updatedId = postService.partialUpdatePostById(postId, post);

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
    public ResponseEntity<Void> deleteUserPost(@PathVariable Long id, @PathVariable Long postId) throws UserNotFoundException {

        var user = userService.getUserById(id); // check if user exists
        var post = postService.getPostByIdForUserById(postId, id); // check if post exists for user

        postService.removePostByUserId(postId, id);
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

        return Stream.of(comment)
                .map(commentModelAssembler::toModel)
                .map(ResponseEntity::ok).findFirst()
                .orElseThrow(() -> new CommentNotFoundException("id-" + commentId + " for user id-" + id));
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
    public ResponseEntity<Void> deleteUserComment(@PathVariable Long id, @PathVariable Long commentId) throws UserNotFoundException, CommentNotFoundForUserException {
        var user = userService.getUserById(id); // check if user exists
        var comment = commentService.getCommentByIdAndUserId(commentId, id); // check if comment exists for user

        commentService.removeCommentByIdAndUserId(commentId, id);
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
    public ResponseEntity<Long> updateCommentForUser(@PathVariable Long id, @PathVariable Long commentId, @Valid @RequestBody Comment comment) throws UserNotFoundException, CommentNotFoundForUserException {
        var user = userService.getUserById(id);

        comment.setAuthor(user);

        Long updatedId = commentService.partialUpdateCommentById(commentId, comment);

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
    public ResponseEntity<Long> createSubReddit(@PathVariable Long id, @Valid @RequestBody SubReddit subReddit) throws UserNotFoundException {
        var user = userService.getUserById(id);

        subReddit.setOwner(user);

        Long createdId = subRedditService.createNewSubReddit(subReddit);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdId).toUri();

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
    public ResponseEntity<Long> updateSubReddit(@PathVariable Long id, @PathVariable Long subRedditId, @Valid @RequestBody SubReddit subReddit) throws UserNotFoundException, SubRedditNotFoundException {
        var user = userService.getUserById(id);

        subReddit.setOwner(user);

        Long updatedId = subRedditService.fullUpdateSubRedditById(subRedditId, subReddit);
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
    public ResponseEntity<Long> partialUpdateSubReddit(@PathVariable Long id, @PathVariable Long subRedditId, @Valid @RequestBody SubReddit subReddit) throws UserNotFoundException, SubRedditNotFoundException {
        var user = userService.getUserById(id);

        subReddit.setOwner(user);

        Long updatedId = subRedditService.partialUpdateSubRedditById(subRedditId, subReddit);
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
    public ResponseEntity<Void> deleteSubReddit(@PathVariable Long id, @PathVariable Long subRedditId) throws UserNotFoundException, SubRedditNotFoundException {
        var user = userService.getUserById(id); // check if user exists
        var subReddit = subRedditService.getSubRedditById(subRedditId); // check if subReddit exists

        subRedditService.removeSubRedditById(subRedditId);
        return ResponseEntity.noContent().build();
    }

}
