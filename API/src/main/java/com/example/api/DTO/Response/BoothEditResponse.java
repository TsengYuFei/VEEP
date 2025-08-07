package com.example.api.DTO.Response;

import com.example.api.Entity.Booth;
import com.example.api.Entity.OpenMode;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BoothEditResponse {
    private Integer boothID;
    private Integer expoID;
    private String owner;
    private String name;
    private String avatar;
    private String introduction;
    private List<TagResponse> tags;
    private OpenMode openMode;
    private Boolean openStatus;
    private LocalDateTime openStart;
    private LocalDateTime openEnd;
    private Integer maxParticipants;
    private Boolean display;
    private List<UserListResponse> collaborators;
    private List<UserListResponse> staffs;

    public static BoothEditResponse fromBooth(Booth booth) {
        BoothEditResponse response = new BoothEditResponse();
        response.setBoothID(booth.getBoothID());
        response.setOwner(booth.getOwner().getUserAccount());
        response.setName(booth.getName());
        response.setAvatar(booth.getAvatar());
        response.setIntroduction(booth.getIntroduction());
        response.setOpenMode(booth.getOpenMode());
        response.setOpenStatus(booth.getOpenStatus());
        response.setOpenStart(booth.getOpenStart());
        response.setOpenEnd(booth.getOpenEnd());
        response.setMaxParticipants(booth.getMaxParticipants());
        response.setDisplay(booth.getDisplay());
        return response;
    }
}
