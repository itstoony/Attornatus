package br.com.itstoony.github.api.attornatus.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.format.DateTimeParseException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFound(EntityNotFoundException ex, HttpServletRequest request) {

        var error = DefaultError.builder()
                .timestamp(System.currentTimeMillis())
                .status(BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(BAD_REQUEST).body(error);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> duplicateInsert(SQLIntegrityConstraintViolationException ex, HttpServletRequest request) {

        var error = DefaultError.builder()
                .timestamp(System.currentTimeMillis())
                .status(BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<?> invalidDateType(DateTimeParseException ex, HttpServletRequest request) {

        var error = DefaultError.builder()
                .timestamp(System.currentTimeMillis())
                .status(BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(BAD_REQUEST).body(error);
    }
}
