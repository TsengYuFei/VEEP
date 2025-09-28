package com.example.api.DTO.Response;

import com.example.api.Entity.Expo;
import com.example.api.Entity.OpenMode;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpoHotResponse {
    private Integer expoID;
    private String name;
    private String avatar;
    private Boolean isOpening;
    private OpenMode openMode;
    private Boolean openStatus;
    private LocalDateTime openStart;
    private LocalDateTime openEnd;
    private Integer onlineParticipants;

    public static ExpoHotResponse fromExpo(Expo expo, Boolean isOpening, Integer onlineParticipants) {
        ExpoHotResponse response = new ExpoHotResponse();
        response.setExpoID(expo.getExpoID());
        response.setName(expo.getName());
        response.setAvatar(expo.getAvatar());
        response.setIsOpening(isOpening);
        response.setOpenMode(expo.getOpenMode());
        response.setOpenStatus(expo.getOpenStatus());
        response.setOpenStart(expo.getOpenStart());
        response.setOpenEnd(expo.getOpenEnd());
        response.setOnlineParticipants(onlineParticipants);
        return response;
    }
}
