package com.example.backendclonereddit.utils.exceptions.types;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ReplyNotFoundException extends RuntimeException {
    public ReplyNotFoundException(String message) {
        super("Reply not found with " + message);
    }
}
