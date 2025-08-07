package com.example.api.DTO.Response;

import com.example.api.Entity.Booth;
import lombok.Data;

@Data
public class BoothOverviewResponse {
    private Integer boothID;
    private Integer expoID;
    private String owner;
    private String name;

    public static BoothOverviewResponse fromBooth(Booth booth) {
        BoothOverviewResponse response = new BoothOverviewResponse();
        response.setBoothID(booth.getBoothID());
        response.setOwner(booth.getOwner().getUserAccount());
        response.setName(booth.getName());

        if(booth.getExpo() == null) response.setExpoID(null);
        else response.setExpoID(booth.getExpo().getExpoID());

        return response;
    }
}
