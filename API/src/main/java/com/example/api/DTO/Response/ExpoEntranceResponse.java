package com.example.api.DTO.Response;

import com.example.api.Entity.Expo;
import com.example.api.Entity.OpenMode;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExpoEntranceResponse {
    private String ownerName;
    private String ownerAvatar;
    private String name;
    private String avatar;
    private String introduction;
    private LocalDateTime openStart;
    private LocalDateTime openEnd;
    private Boolean isOpening;
    private Boolean needVerification;

    public static ExpoEntranceResponse fromExpo(Expo expo){
        ExpoEntranceResponse response = new ExpoEntranceResponse();
        response.setOwnerName(expo.getOwner().getName());
        response.setOwnerAvatar(expo.getOwner().getAvatar());
        response.setName(expo.getName());
        response.setAvatar(expo.getAvatar());
        response.setIntroduction(expo.getIntroduction());
        response.setOpenStart(null);
        response.setOpenEnd(null);
        response.setIsOpening(false);
        response.setNeedVerification(false);
        return response;
    }
}
