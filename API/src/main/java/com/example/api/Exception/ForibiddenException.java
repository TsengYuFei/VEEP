package com.example.api.Exception;

// 403 - 權限不足
public class ForibiddenException extends RuntimeException{
    public ForibiddenException(String message){
        super(message);
    }
}
