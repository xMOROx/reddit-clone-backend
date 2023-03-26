package com.example.backendclonereddit.resources;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.Post;
import com.example.backendclonereddit.repositories.PostRepository;
import com.example.backendclonereddit.utils.exceptions.PostNotFoundException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
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
    public MappingJacksonValue getAllPosts() {
        MappingJacksonValue mapping = new MappingJacksonValue(postRepository.findAll());
        var simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "title", "content", "user", "comments");

        FilterProvider filters = new SimpleFilterProvider().addFilter("postFilter",simpleBeanPropertyFilter );

        mapping.setFilters(filters);

        return mapping;
    }

    @GetMapping(path = "/{id}")
    public MappingJacksonValue getPostById(@PathVariable Long id) throws PostNotFoundException {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new PostNotFoundException("id-" + id);
        }

        MappingJacksonValue mapping = new MappingJacksonValue(post.get());
        var simpleBeanPropertyFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "title", "content", "user", "comments");

        FilterProvider filters = new SimpleFilterProvider().addFilter("postFilter", simpleBeanPropertyFilter);

        mapping.setFilters(filters);

        return mapping;
    }

}
