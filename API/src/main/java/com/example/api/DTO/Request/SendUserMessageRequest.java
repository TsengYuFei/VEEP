package com.example.api.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SendUserMessageRequest {
    @NotBlank(message = "The receiver's user account cannot be empty.")
    @Size(min = 6, max = 30, message = "使用者帳號長度應該在6~20字元之間")
    private String receiverAccount;

    @NotBlank
    private String message;
}
