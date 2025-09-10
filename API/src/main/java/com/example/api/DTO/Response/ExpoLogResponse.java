package com.example.api.DTO.Response;

import com.example.api.Entity.ExpoLog;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpoLogResponse {
    private String sessionID;
    private String userAccount;
    private Integer expoID;
    private LocalDateTime enterAt;
    private LocalDateTime exitAt;
    private LocalDateTime lastActiveAt;
    private Boolean hasUsedAi;
    private Integer aiMessageCount;
    private String role;
    private Boolean isOwner;
    private Boolean isCollaborator;
    private Boolean isWhiteListed;


    public static ExpoLogResponse fromExpoLog(ExpoLog expoLog) {
        ExpoLogResponse response = new ExpoLogResponse();

        response.setSessionID(expoLog.getSessionID());
        response.setUserAccount(expoLog.getUser().getUserAccount());
        response.setExpoID(expoLog.getExpo().getExpoID());
        response.setEnterAt(expoLog.getEnterAt());
        response.setExitAt(expoLog.getExitAt());
        response.setLastActiveAt(expoLog.getLastActiveAt());
        response.setHasUsedAi(expoLog.getHasUsedAi());
        response.setAiMessageCount(expoLog.getAiMessageCount());
        response.setRole(expoLog.getRole());
        response.setIsOwner(expoLog.getIsOwner());
        response.setIsCollaborator(expoLog.getIsCollaborator());
        response.setIsWhiteListed(expoLog.getIsWhiteListed());

        return response;
    }
}
