package com.example.api.DTO.Request;

import lombok.Data;

@Data
public class BoothLogUpdateRequest {

    private Boolean isExit;

    private Boolean isActive;

    private Boolean isUsedAI;
}
