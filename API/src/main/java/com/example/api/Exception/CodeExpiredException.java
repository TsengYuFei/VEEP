package com.example.api.Exception;

// 410 - (驗證碼或資源已過期)
public class CodeExpiredException extends RuntimeException {
    public CodeExpiredException(String message) {
        super(message);
    }
}
