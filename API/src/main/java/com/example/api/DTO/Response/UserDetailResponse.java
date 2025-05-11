package com.example.api.DTO.Response;

import com.example.api.Entity.Role;
import com.example.api.Entity.User;
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

    public static UserDetailResponse fromUser(User user) {
        UserDetailResponse response = new UserDetailResponse();

        response.name = user.getName();
        response.userAccount = user.getUserAccount();
        response.avatar = user.getAvatar();
        response.bio = user.getBio();
        response.background = user.getBackground();
        response.showFollowers = user.getShowFollowers();
        response.showFollowing = user.getShowFollowing();
        response.showHistory = user.getShowHistory();
        response.showCurrentExpo = user.getShowCurrentExpo();
        response.showCurrentBooth = user.getShowCurrentBooth();
        response.role = user.getRole();

        return response;
    }
}