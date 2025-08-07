package com.example.api.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendExpoGroupMessageRequest {
    @NotBlank
    private String message;
}
