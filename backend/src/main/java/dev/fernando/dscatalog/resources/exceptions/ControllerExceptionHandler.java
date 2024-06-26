package dev.fernando.dscatalog.resources.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dev.fernando.dscatalog.services.exceptions.DatabaseException;
import dev.fernando.dscatalog.services.exceptions.ResourceNotFoundException;
import dev.fernando.dscatalog.services.exceptions.StandardError;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        int status = HttpStatus.NOT_FOUND.value();
        String path = request.getRequestURI();
        return ResponseEntity.status(status).body(new StandardError(status, e.getMessage(), Instant.now(), path));
    }
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> handleDatabaseException(DatabaseException e, HttpServletRequest request) {
        int status = HttpStatus.UNPROCESSABLE_ENTITY.value();
        String path = request.getRequestURI();
        return ResponseEntity.status(status).body(new StandardError(status, e.getMessage(), Instant.now(), path));
    }
}
