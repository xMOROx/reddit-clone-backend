package com.example.backendclonereddit.utils.exceptions;

import java.time.LocalDateTime;

public class ErrorDetails {
    private final String message;
    private final String details;

    private final LocalDateTime timestamp;

    public ErrorDetails(String message, String details, LocalDateTime timestamp) {
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDetails() {
        return details;
    }
}
