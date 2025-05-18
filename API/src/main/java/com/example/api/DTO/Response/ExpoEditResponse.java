package com.example.api.DTO.Response;

import com.example.api.Entity.Expo;
import com.example.api.Entity.OpenMode;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Component
public class ExpoEditResponse {
    private Integer expoID;
    private String owner;
    private String name;
    private String avatar;
    private Integer price;
    private String introduction;
    private List<TagResponse> tags;
    private OpenMode openMode;
    private Boolean openStatus;
    private LocalDateTime openStart;
    private LocalDateTime openEnd;
    private String accessCode;
    private Integer maxParticipants;
    private Boolean display;
    private List<UserListResponse> collaborators;
    private List<UserListResponse> whitelist;
    private List<UserListResponse> blacklist;

    public static ExpoEditResponse fromExpo(Expo expo){
        ExpoEditResponse response = new ExpoEditResponse();
        response.setExpoID(expo.getExpoID());
        response.setOwner(expo.getOwner().getUserAccount());
        response.setName(expo.getName());
        response.setAvatar(expo.getAvatar());
        response.setPrice(expo.getPrice());
        response.setIntroduction(expo.getIntroduction());
        response.setOpenMode(expo.getOpenMode());
        response.setOpenStatus(expo.getOpenStatus());
        response.setOpenStart(expo.getOpenStart());
        response.setOpenEnd(expo.getOpenEnd());
        response.setAccessCode(expo.getAccessCode());
        response.setMaxParticipants(expo.getMaxParticipants());
        response.setDisplay(expo.getDisplay());
        return response;
    }
}
