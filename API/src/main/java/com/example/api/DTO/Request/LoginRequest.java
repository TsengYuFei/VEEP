package com.example.api.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "The user account or email cannot be empty.")
    private String userAccountOrMail;

    @NotBlank(message = "The password cannot be empty.")
    private String password;
}
