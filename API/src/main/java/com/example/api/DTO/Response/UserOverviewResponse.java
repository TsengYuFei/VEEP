package com.example.api.DTO.Response;

import com.example.api.Entity.Role;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserOverviewResponse {
    private String name;
    private String userAccount;
    private String avatar;
    private Role role;
}
