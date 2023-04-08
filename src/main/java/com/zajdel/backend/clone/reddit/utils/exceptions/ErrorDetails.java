package com.example.backendclonereddit.utils.exceptions;

import java.time.LocalDateTime;

public record ErrorDetails(String message, String details, LocalDateTime timestamp) {
}
