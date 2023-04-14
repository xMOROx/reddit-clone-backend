package com.zajdel.backend.clone.reddit.controllers;

import com.zajdel.backend.clone.reddit.configs.ApiPaths;
import com.zajdel.backend.clone.reddit.entities.SubReddit;
import com.zajdel.backend.clone.reddit.models.SubRedditModel;
import com.zajdel.backend.clone.reddit.services.SubRedditService;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.SubRedditNotFoundException;
import com.zajdel.backend.clone.reddit.utils.models.assemblers.SubRedditModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = ApiPaths.SubRedditCtrl.CTRL)
public class SubRedditController {

    private final SubRedditService subRedditService;
    private final SubRedditModelAssembler subRedditModelAssembler;

    public SubRedditController(SubRedditService subRedditService,
                               SubRedditModelAssembler subRedditModelAssembler) {
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
    public ResponseEntity<Void> deleteSubRedditById(@PathVariable Long id) {
        subRedditService.removeSubRedditById(id);
        return ResponseEntity.noContent().build();
    }
}
