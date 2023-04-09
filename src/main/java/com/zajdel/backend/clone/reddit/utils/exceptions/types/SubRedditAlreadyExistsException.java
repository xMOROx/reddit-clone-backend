package com.zajdel.backend.clone.reddit.utils.exceptions.types;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Subreddit already exists", code = HttpStatus.CONFLICT)
public class SubRedditAlreadyExistsException extends RuntimeException {
    public SubRedditAlreadyExistsException(String message) {
        super(message);
    }
}
