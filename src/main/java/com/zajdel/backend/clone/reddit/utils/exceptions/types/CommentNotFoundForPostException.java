package com.zajdel.backend.clone.reddit.utils.exceptions.types;

public class CommentNotFoundForPostException extends RuntimeException
{
    public CommentNotFoundForPostException(String message) {
        super("Comment not found for post with: " + message);
    }
}
