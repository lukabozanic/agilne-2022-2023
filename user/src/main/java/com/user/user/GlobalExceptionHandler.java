package com.user.user;

import com.user.user.CustomExceptions.EntityDoesntExistException;
import com.user.user.CustomExceptions.EntiyWithEmailAlreadyExistsException;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(EntiyWithEmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityWithEmailAlreadyExistsException(EntiyWithEmailAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(errorResponse);
    }
	
	@ExceptionHandler(EntityDoesntExistException.class)
    public ResponseEntity<ErrorResponse> handleEntityDoesntExist(EntityDoesntExistException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.MethodExecutionPermissionDeniedException.class)
    public ResponseEntity<ErrorResponse> handleMethodExecutionPermissionDenied(CustomExceptions.MethodExecutionPermissionDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.OwnerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleOwnerAlreadyExists(CustomExceptions.OwnerAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(errorResponse);
    }

    // Add more exception handlers for other custom exceptions if needed

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    	ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
