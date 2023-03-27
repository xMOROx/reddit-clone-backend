package com.example.backendclonereddit.resources;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.Post;
import com.example.backendclonereddit.models.PostModel;
import com.example.backendclonereddit.repositories.PostRepository;
import com.example.backendclonereddit.utils.exceptions.PostNotFoundException;
import com.example.backendclonereddit.utils.models.assemblers.PostModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = ApiPaths.PostCtrl.CTRL)
public class PostResource {
    private final PostRepository postRepository;
    private final PostModelAssembler postModelAssembler;

    public PostResource(PostRepository postRepository, PostModelAssembler postModelAssembler) {
        this.postRepository = postRepository;
        this.postModelAssembler = postModelAssembler;
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

}
