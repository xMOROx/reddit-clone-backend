package com.example.backendclonereddit.utils.exceptions.types;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubRedditNotFoundException extends RuntimeException {

    public SubRedditNotFoundException(String message) {
        super("SubReddit not found with: " + message);
    }

}
