package com.alweimine.supportticketsystem.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerException {
    private static final Logger logger = LoggerFactory.getLogger(ControllerException.class);

    @ExceptionHandler(DuplicateUserNameException.class)
    public ResponseEntity<DuplicateUserNameException> duplicateUserNameHandler(
            DuplicateUserNameException duplicateUserNameException, WebRequest request) {
        // Log the exception details
        logger.error("DuplicateUserNameException occurred: {}, Request Details: {}",
                duplicateUserNameException.getMessage(), request.getDescription(false), duplicateUserNameException);

        // Return ResponseEntity with the exception and HTTP status 409 Conflict
        return new ResponseEntity<>(duplicateUserNameException, HttpStatus.CONFLICT);
    }

    /**
     * Handles BusinessException globally.
     * This handler returns a 400 Bad Request status and logs the exception details.
     *
     * @param businessException The exception that was thrown
     * @param request The current HTTP request details
     * @return ResponseEntity containing the exception and HTTP status 400 Bad Request
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BusinessException> businessExceptionHandler(
            BusinessException businessException, WebRequest request) {
        // Log the exception details
        logger.error("BusinessException occurred: {}, Request Details: {}",
                businessException.getMessage(), request.getDescription(false), businessException);

        // Return ResponseEntity with the exception and HTTP status 400 Bad Request
        return new ResponseEntity<>(businessException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> genericExceptionHandler(Exception exception, WebRequest request) {
        // Log the exception details
        logger.error("Exception occurred: {}, Request Details: {}", exception.getMessage(),
                request.getDescription(false), exception);

        // Return a generic response with exception message and HTTP status 400 Bad Request
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
