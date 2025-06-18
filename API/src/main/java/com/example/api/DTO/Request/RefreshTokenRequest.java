package com.example.api.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "RefreshToken cannot be empty.")
    private String refreshToken;
}
