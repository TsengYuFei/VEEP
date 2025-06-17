package com.example.api.Other;

import com.example.api.DTO.Response.ErrorResponseResponse;
import com.example.api.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {
    // 400 - 格式請求錯誤、欄位缺失、不合法等
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseResponse> handleBadRequestException(BadRequestException exception) {
        ErrorResponseResponse error = new ErrorResponseResponse("Bad Request", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 409 - (帳號已存在)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseResponse> handleConflictException(ConflictException exception) {
        ErrorResponseResponse error = new ErrorResponseResponse("Conflict", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // 403 - 權限不足(手動丟)
    @ExceptionHandler(ForibiddenException.class)
    public ResponseEntity<ErrorResponseResponse> handleForbiddenException(ForibiddenException exception) {
        ErrorResponseResponse error = new ErrorResponseResponse("Forbidden", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // 403 - 權限不足(Spring Security丟)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseResponse> handleAccessDeniedException(AccessDeniedException exception) {
        ErrorResponseResponse error = new ErrorResponseResponse("Forbidden", "權限不足，禁止存取此資源。");
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // 404 - 找不到
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseResponse> handleNotFoundException(NotFoundException exception) {
        ErrorResponseResponse error = new ErrorResponseResponse("Not Found", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 401 - 認證失敗，沒有登入或token錯誤
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseResponse> handleUnauthorizedException(UnauthorizedException exception) {
        ErrorResponseResponse error = new ErrorResponseResponse("Unauthorized", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    // 422 - 格式正確，但內容不符合商業規則
    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ErrorResponseResponse> handleUnprocessableEntityException(UnprocessableEntityException exception) {
        ErrorResponseResponse error = new ErrorResponseResponse("Unprocessable Entity", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // enum
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "請確認傳入的資料格式是否正確（可能為 enum 或日期）";
        return new ResponseEntity<>(new ErrorResponseResponse("格式錯誤", message), HttpStatus.BAD_REQUEST);
    }

    // valid驗證
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder messageBuilder = new StringBuilder();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            messageBuilder.append(error.getDefaultMessage()).append("；");
        }

        String message = messageBuilder.toString();
        if (message.endsWith("；")) {
            message = message.substring(0, message.length() - 1);
        }

        ErrorResponseResponse error = new ErrorResponseResponse("欄位驗證錯誤", message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseResponse> handle(Exception exception) {
        ErrorResponseResponse error = new ErrorResponseResponse("Internal Server Error", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
