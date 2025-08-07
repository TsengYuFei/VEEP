package com.example.api.DTO.Response;

import com.example.api.Entity.Expo;
import lombok.Data;

@Data
public class ExpoOverviewResponse {
    private Integer expoID;
    private String owner;
    private String name;

    public static ExpoOverviewResponse fromExpo(Expo expo) {
        ExpoOverviewResponse response = new ExpoOverviewResponse();
        response.setExpoID(expo.getExpoID());
        response.setOwner(expo.getOwner().getUserAccount());
        response.setName(expo.getName());
        return response;
    }
}
