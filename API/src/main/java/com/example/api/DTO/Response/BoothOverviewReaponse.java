package com.example.api.DTO.Response;

import com.example.api.Entity.Booth;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class BoothOverviewReaponse {
    private Integer boothID;
    private Integer expoID;
    private String owner;
    private String name;

    public static BoothOverviewReaponse fromBooth(Booth booth) {
        BoothOverviewReaponse response = new BoothOverviewReaponse();
        response.setBoothID(booth.getBoothID());
        response.setOwner(booth.getOwner().getUserAccount());
        response.setName(booth.getName());
        return response;
    }
}
