package com.currency.exchange.currencyexchange;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(CustomExceptions.EntiyWithEmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityWithEmailAlreadyExistsException(CustomExceptions.EntiyWithEmailAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(errorResponse);
    }
	
	@ExceptionHandler(CustomExceptions.EntityDoesntExistException.class)
    public ResponseEntity<ErrorResponse> handleEntityDoesntExist(CustomExceptions.EntityDoesntExistException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(CustomExceptions.MethodExecutionPermissionDenied.class)
    public ResponseEntity<ErrorResponse> handleMethodExecutionPermissionDenied(CustomExceptions.MethodExecutionPermissionDenied ex) {
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
