package com.example.api.DTO;

import com.example.api.Model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserOverviewDTO {
    private String name;
    private String userAccount;
    private String avatar;
    private Role role;
}
