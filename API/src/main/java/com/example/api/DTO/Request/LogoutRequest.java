package com.example.api.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogoutRequest {

    @NotBlank(message = "The refresh token cannot be empty.")
    private String refreshToken;
}
