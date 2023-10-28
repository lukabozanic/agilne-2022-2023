package com.bank.account.bankaccount;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bank.account.bankaccount.CustomExceptions.EntityDoesntExistException;
import com.bank.account.bankaccount.CustomExceptions.EntiyWithEmailAlreadyExistsException;

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

    // Add more exception handlers for other custom exceptions if needed

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    	ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
