package com.example.api.Controller;

import com.example.api.DTO.ErrorResponseDTO;
import com.example.api.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException exception) {
        ErrorResponseDTO error = new ErrorResponseDTO("Bad Request", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflictException(ConflictException exception) {
        ErrorResponseDTO error = new ErrorResponseDTO("Conflict", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForibiddenException.class)
    public ResponseEntity<ErrorResponseDTO> handleForbiddenException(ForibiddenException exception) {
        ErrorResponseDTO error = new ErrorResponseDTO("Forbidden", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException exception) {
        ErrorResponseDTO error = new ErrorResponseDTO("Not Found", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(UnauthorizedException exception) {
        ErrorResponseDTO error = new ErrorResponseDTO("Unauthorized", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnprocessableEntityException(UnprocessableEntityException exception) {
        ErrorResponseDTO error = new ErrorResponseDTO("Unprocessable Entity", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "請確認傳入的資料格式是否正確（可能為 enum 或日期）";
        return new ResponseEntity<>(new ErrorResponseDTO("格式錯誤", message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handle(Exception exception) {
        ErrorResponseDTO error = new ErrorResponseDTO("Internal Server Error", exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
