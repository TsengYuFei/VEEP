package com.example.api.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
public class ExpoLogCreateResponse {
    private String sessionID;
}
