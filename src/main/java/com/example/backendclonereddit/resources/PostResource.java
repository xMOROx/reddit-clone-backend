package com.example.backendclonereddit.resources;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.Post;
import com.example.backendclonereddit.repositories.PostRepository;
import com.example.backendclonereddit.utils.exceptions.PostNotFoundException;
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

    public PostResource(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping(path = "")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Post getPostById(@PathVariable Long id) throws PostNotFoundException {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new PostNotFoundException("id-" + id);
        }

        return post.get();
    }

}
