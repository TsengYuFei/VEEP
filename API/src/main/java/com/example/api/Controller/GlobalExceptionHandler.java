package com.example.api.Controller;

import com.example.api.DTO.ErrorResponseDTO;
import com.example.api.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // 400 - 格式請求錯誤、欄位缺失、不合法等
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException exception) {
        ErrorResponseDTO error = new ErrorResponseDTO("Bad Request", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 409 - (帳號已存在)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflictException(ConflictException exception) {
        ErrorResponseDTO error = new ErrorResponseDTO("Conflict", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // 403 - 權限不足
    @ExceptionHandler(ForibiddenException.class)
    public ResponseEntity<ErrorResponseDTO> handleForbiddenException(ForibiddenException exception) {
        ErrorResponseDTO error = new ErrorResponseDTO("Forbidden", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    // 404 - 找不到
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException exception) {
        ErrorResponseDTO error = new ErrorResponseDTO("Not Found", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // 401 - 認證失敗，沒有登入或token錯誤
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(UnauthorizedException exception) {
        ErrorResponseDTO error = new ErrorResponseDTO("Unauthorized", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    // 422 - 格式正確，但內容不符合商業規則
    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnprocessableEntityException(UnprocessableEntityException exception) {
        ErrorResponseDTO error = new ErrorResponseDTO("Unprocessable Entity", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // enum
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "請確認傳入的資料格式是否正確（可能為 enum 或日期）";
        return new ResponseEntity<>(new ErrorResponseDTO("格式錯誤", message), HttpStatus.BAD_REQUEST);
    }

    // valid驗證
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder messageBuilder = new StringBuilder();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            messageBuilder.append(error.getDefaultMessage()).append("；");
        }

        String message = messageBuilder.toString();
        if (message.endsWith("；")) {
            message = message.substring(0, message.length() - 1);
        }

        ErrorResponseDTO error = new ErrorResponseDTO("欄位驗證錯誤", message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handle(Exception exception) {
        ErrorResponseDTO error = new ErrorResponseDTO("Internal Server Error", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
