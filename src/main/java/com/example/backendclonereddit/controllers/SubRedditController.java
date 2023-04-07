package com.example.backendclonereddit.controllers;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.SubReddit;
import com.example.backendclonereddit.models.SubRedditModel;
import com.example.backendclonereddit.services.SubRedditService;
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

    @GetMapping("")
    public ResponseEntity<CollectionModel<SubRedditModel>> getAllSubReddits() {
        List< SubReddit> subReddits = subRedditService.getAllSubReddits();
        return new ResponseEntity<>(
                subRedditModelAssembler.toCollectionModel(subReddits),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubRedditModel> getSubRedditById(@PathVariable Long id) {
        SubReddit subReddit = subRedditService.getSubRedditById(id);
        return new ResponseEntity<>(
                subRedditModelAssembler.toModel(subReddit),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SubRedditModel> deleteSubRedditById(@PathVariable Long id) {
        subRedditService.remove(id);
        return ResponseEntity.noContent().build();
    }


}
