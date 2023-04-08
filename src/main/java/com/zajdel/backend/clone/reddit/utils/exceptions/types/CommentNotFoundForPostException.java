package com.example.backendclonereddit.utils.exceptions.types;

public class CommentNotFoundForPostException extends RuntimeException
{
    public CommentNotFoundForPostException(String message) {
        super("Comment with " + message + " not found for post");
    }
}
