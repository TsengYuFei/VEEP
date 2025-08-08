package com.example.api.DTO.Response;

import com.example.api.Entity.Booth;
import lombok.Data;

@Data
public class BoothOverviewResponse {
    private Integer boothID;
    private Integer expoID;
    private String name;
    private String avatar;
    private String introduction;

    public static BoothOverviewResponse fromBooth(Booth booth) {
        BoothOverviewResponse response = new BoothOverviewResponse();
        response.setBoothID(booth.getBoothID());
        response.setName(booth.getName());
        response.setAvatar(booth.getAvatar());
        response.setIntroduction(booth.getIntroduction());

        if(booth.getExpo() == null) response.setExpoID(null);
        else response.setExpoID(booth.getExpo().getExpoID());

        return response;
    }
}
