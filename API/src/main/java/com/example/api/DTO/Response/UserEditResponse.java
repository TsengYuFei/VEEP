package com.example.api.DTO.Response;

import com.example.api.Entity.Role;
import com.example.api.Entity.User;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Component
public class UserEditResponse {
    private String name;
    private String userAccount;
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

    public static UserEditResponse fromUser(User user) {
        UserEditResponse response = new UserEditResponse();

        response.name = user.getName();
        response.userAccount = user.getUserAccount();
        response.tel = user.getTel();
        response.mail = user.getMail();
        response.avatar = user.getAvatar();
        response.birthday = user.getBirthday();
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
