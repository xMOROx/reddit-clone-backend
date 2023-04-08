package com.zajdel.backend.clone.reddit.utils.exceptions.types;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundForUserException extends RuntimeException {
    public CommentNotFoundForUserException(String message) {
        super("Comment with " + message + " not found for user");
    }
}
