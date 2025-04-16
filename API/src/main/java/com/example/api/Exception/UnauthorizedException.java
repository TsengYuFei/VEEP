package com.example.api.Exception;

// 401 - 認證失敗，沒有登入或token錯誤
public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
}
