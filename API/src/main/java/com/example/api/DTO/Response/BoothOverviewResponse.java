package com.example.api.DTO.Response;

import com.example.api.Entity.Booth;
import com.example.api.Entity.Expo;
import lombok.Data;

@Data
public class BoothOverviewResponse {
    private Integer boothID;
    private Integer expoID;
    private String expoName;
    private String name;
    private String avatar;

    public static BoothOverviewResponse fromBooth(Booth booth) {
        BoothOverviewResponse response = new BoothOverviewResponse();
        response.setBoothID(booth.getBoothID());
        response.setName(booth.getName());
        response.setAvatar(booth.getAvatar());

        if(booth.getExpo() == null) response.setExpoID(null);
        else {
            Expo expo = booth.getExpo();
            response.setExpoID(expo.getExpoID());
            response.setExpoName(expo.getName());
        }

        return response;
    }
}
