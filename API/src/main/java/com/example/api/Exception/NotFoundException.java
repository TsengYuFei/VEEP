package com.example.api.Exception;

// 404 - 找不到
public class NotFoundException extends RuntimeException{
    public NotFoundException(String message) {
        super(message);
    }
}
