package com.example.ecommerce.exception;

public class InvalidPaginationException extends RuntimeException{
    public InvalidPaginationException(String message) {
        super(message);
    }
}
