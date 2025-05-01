package com.example.api.DTO.Response;

import com.example.api.Entity.Role;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Component
public class UserEditResponse {
    private String name;
    private String tel;
    private String mail;
    private String avatar;
    private LocalDate birthday;
    private String bio;
    private String background;
    private Boolean showFollowers;
    private Boolean showFollowing;
    private Boolean showHistory;
    private Boolean showCurrentExpo;
    private Boolean showCurrentBooth;
    private Role role;
}
