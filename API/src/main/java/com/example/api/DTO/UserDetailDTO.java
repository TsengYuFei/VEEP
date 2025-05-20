package com.example.api.DTO;

import com.example.api.Model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserDetailDTO {
    private String name;
    private String userAccount;
    private String avatar;
    private String bio;
    private String background;
    private boolean showFollowers;
    private boolean showFollowing;
    private boolean showHistory;
    private boolean showCurrentExpo;
    private boolean showCurrentBooth;
    private Role role;
}