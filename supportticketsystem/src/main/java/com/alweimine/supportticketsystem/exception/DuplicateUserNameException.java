package com.alweimine.supportticketsystem.exception;

public class DuplicateUserNameException extends BusinessException {

    /**
     * Constructor for creating a new DuplicateUserNameException.
     *
     * @param code The error code associated with this exception
     * @param message The error message explaining the cause of the exception
     */
    public DuplicateUserNameException(int code, String message) {
        // Call the superclass (BusinessException) constructor with the provided code and message
        super(code, message);
    }
}