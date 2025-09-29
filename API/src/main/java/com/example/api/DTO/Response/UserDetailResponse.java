package com.example.api.DTO.Response;

import com.example.api.Entity.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDetailResponse {
    private String name;
    private String userAccount;
    private String avatar;
    private String bio;
    private String background;
    private Boolean showHistory;
    private Boolean showCurrentExpo;
    private Boolean showCurrentBooth;
    private List<Integer> historyExpoID;
    private List<Integer> currentExpoID;
    private List<Integer> currentBoothID;
    private String roleName;

    public static UserDetailResponse fromUser(User user) {
        UserDetailResponse response = new UserDetailResponse();

        response.name = user.getName();
        response.userAccount = user.getUserAccount();
        response.avatar = user.getAvatar();
        response.bio = user.getBio();
        response.background = user.getBackground();
        response.showHistory = user.getShowHistory();
        response.showCurrentExpo = user.getShowCurrentExpo();
        response.showCurrentBooth = user.getShowCurrentBooth();
        response.historyExpoID = new ArrayList<>();
        response.currentExpoID = new ArrayList<>();
        response.currentBoothID = new ArrayList<>();

        return response;
    }
}