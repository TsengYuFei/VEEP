package com.example.api.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String userAccount;
    private String accessToken;
    private String refreshToken;
}
