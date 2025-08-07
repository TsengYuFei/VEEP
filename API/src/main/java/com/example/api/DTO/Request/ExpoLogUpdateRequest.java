package com.example.api.DTO.Request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpoLogUpdateRequest {

    private Boolean isExit;

    private Boolean isUsedAI;
}
