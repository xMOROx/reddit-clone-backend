package com.example.backendclonereddit.controllers;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.SubReddit;
import com.example.backendclonereddit.models.SubRedditModel;
import com.example.backendclonereddit.services.SubRedditService;
import com.example.backendclonereddit.utils.exceptions.types.SubRedditNotFoundException;
import com.example.backendclonereddit.utils.models.assemblers.SubRedditModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = ApiPaths.SubRedditCtrl.CTRL)
public class SubRedditController {

    private final SubRedditService subRedditService;
    private final SubRedditModelAssembler subRedditModelAssembler;

    public SubRedditController(SubRedditService subRedditService, SubRedditModelAssembler subRedditModelAssembler) {
        this.subRedditService = subRedditService;
        this.subRedditModelAssembler = subRedditModelAssembler;
    }

    /**
     * Get all subreddits
     *
     * @return List of subreddits and Response ok
     */
    @GetMapping("")
    public ResponseEntity<CollectionModel<SubRedditModel>> getAllSubReddits() {
        List<SubReddit> subReddits = subRedditService.getAllSubReddits();
        return new ResponseEntity<>(
                subRedditModelAssembler.toCollectionModel(subReddits),
                HttpStatus.OK
        );
    }

    /**
     * Get subreddit by id
     *
     * @param id Subreddit id
     * @return Response ok
     * @throws SubRedditNotFoundException if subreddit not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubRedditModel> getSubRedditById(@PathVariable Long id) throws SubRedditNotFoundException {
        SubReddit subReddit = subRedditService.getSubRedditById(id);
        return new ResponseEntity<>(
                subRedditModelAssembler.toModel(subReddit),
                HttpStatus.OK
        );
    }

    /**
     * Delete subreddit by id
     *
     * @param id Subreddit id
     * @return Response no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<SubRedditModel> deleteSubRedditById(@PathVariable Long id) {
        subRedditService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
