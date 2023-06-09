package com.zajdel.backend.clone.reddit.utils.exceptions;

import com.zajdel.backend.clone.reddit.utils.exceptions.types.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

@ControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now());

        return new ResponseEntity<>(
                errorDetails,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(
            {UserNotFoundException.class,
                    PostNotFoundException.class,
                    CommentNotFoundException.class,
                    PostNotFoundForUserException.class,
                    CommentNotFoundForUserException.class,
                    CommentNotFoundForPostException.class,
                    ReplyNotFoundException.class,
                    SubRedditNotFoundException.class}
    )
    public final ResponseEntity<ErrorDetails> handleNotFoundExceptions(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserAlreadyExistsException.class, SubRedditAlreadyExistsException.class})
    public final ResponseEntity<ErrorDetails> handleUserAlreadyExistsException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                ex.getMessage(),
                request.getDescription(false),
                LocalDateTime.now());

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ErrorDetails> handleDataIntegrityViolation(Exception ex, WebRequest request) {
        StringBuilder message = new StringBuilder();
        message.append("Data integrity violation: ");

//        TODO: implement this

        ErrorDetails errorDetails = new ErrorDetails(
                message.toString(),
                request.getDescription(false),
                LocalDateTime.now());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                "Number of errors: " + ex.getErrorCount() + " First one is: "
                        + Objects.requireNonNull(ex.getFieldError()).getDefaultMessage(),
                request.getDescription(false),
                LocalDateTime.now());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

}
