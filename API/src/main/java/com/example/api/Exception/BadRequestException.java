package com.example.api.Exception;

// 400 - 格式請求錯誤、欄位缺失、不合法等
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}
