package com.example.api.DTO.Response;

import com.example.api.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String userAccount;
    private String token;
    private Role role;
}
