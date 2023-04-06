package com.example.backendclonereddit.utils.exceptions.types;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PostNotFoundForUserException extends RuntimeException{

    public PostNotFoundForUserException(String message) {
        super("Post not found with: " +  message);
    }

}
