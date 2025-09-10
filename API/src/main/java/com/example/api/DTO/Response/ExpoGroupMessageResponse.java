package com.example.api.DTO.Response;

import com.example.api.Entity.ExpoGroupMessage;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpoGroupMessageResponse {
    private Integer expoID;
    private String senderAccount;
    private String message;
    private LocalDateTime sendAt;

    public static ExpoGroupMessageResponse fromExpoGroupMessage(ExpoGroupMessage message) {
        ExpoGroupMessageResponse response = new ExpoGroupMessageResponse();
        response.setExpoID(message.getExpo().getExpoID());
        response.setSenderAccount(message.getSender().getUserAccount());
        response.setMessage(message.getMessage());
        response.setSendAt(message.getSendAt());
        return response;
    }
}
