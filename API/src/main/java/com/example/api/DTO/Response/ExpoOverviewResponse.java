package com.example.api.DTO.Response;

import com.example.api.Entity.Expo;
import lombok.Data;

@Data
public class ExpoOverviewResponse {
    private Integer expoID;
    private String name;
    private String avatar;
    private String introduction;
    private Boolean isOpening;

    public static ExpoOverviewResponse fromExpo(Expo expo, Boolean isOpening) {
        ExpoOverviewResponse response = new ExpoOverviewResponse();
        response.setExpoID(expo.getExpoID());
        response.setName(expo.getName());
        response.setAvatar(expo.getAvatar());
        response.setIntroduction(expo.getIntroduction());
        response.setIsOpening(isOpening);
        return response;
    }
}
