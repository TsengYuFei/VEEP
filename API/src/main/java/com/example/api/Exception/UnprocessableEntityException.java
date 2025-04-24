package com.example.api.Exception;

// 422 - 格式正確，但內容不符合商業規則
public class UnprocessableEntityException extends RuntimeException{
    public UnprocessableEntityException(String message){
        super(message);
    }
}
