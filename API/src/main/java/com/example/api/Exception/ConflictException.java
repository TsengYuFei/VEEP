package com.example.api.Exception;

// 409 - (帳號已存在)
public class ConflictException extends RuntimeException{
    public ConflictException(String message){
        super(message);
    }
}
