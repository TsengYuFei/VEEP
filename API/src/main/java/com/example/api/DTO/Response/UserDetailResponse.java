package com.example.api.DTO.Response;

import com.example.api.Entity.Role;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserDetailResponse {
    private String name;
    private String userAccount;
    private String avatar;
    private String bio;
    private String background;
    private Boolean showFollowers;
    private Boolean showFollowing;
    private Boolean showHistory;
    private Boolean showCurrentExpo;
    private Boolean showCurrentBooth;
    private Role role;
}