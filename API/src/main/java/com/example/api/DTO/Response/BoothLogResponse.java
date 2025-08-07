package com.example.api.DTO.Response;

import com.example.api.Entity.BoothLog;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoothLogResponse {
    private String sessionID;
    private String userAccount;
    private Integer expoID;
    private Integer boothID;
    private LocalDateTime enterAt;
    private LocalDateTime exitAt;
    private LocalDateTime lastActiveAt;
    private Boolean hasUsedAi;
    private Integer aiMessageCount;
    private String role;
    private Boolean isOwner;
    private Boolean isCollaborator;


    public static BoothLogResponse fromBoothLog(BoothLog boothLog) {
        BoothLogResponse response = new BoothLogResponse();

        response.setSessionID(boothLog.getSessionID());
        response.setUserAccount(boothLog.getUser().getUserAccount());
        response.setExpoID(boothLog.getExpo().getExpoID());
        response.setBoothID(boothLog.getBooth().getBoothID());
        response.setEnterAt(boothLog.getEnterAt());
        response.setExitAt(boothLog.getExitAt());
        response.setLastActiveAt(boothLog.getLastActiveAt());
        response.setHasUsedAi(boothLog.getHasUsedAi());
        response.setAiMessageCount(boothLog.getAiMessageCount());
        response.setRole(boothLog.getRole());
        response.setIsOwner(boothLog.getIsOwner());
        response.setIsCollaborator(boothLog.getIsCollaborator());

        return response;
    }
}
