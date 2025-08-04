package com.example.api.DTO.Response;

import com.example.api.Entity.ContentLog;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContentLogResponse {
    private String sessionID;
    private String userAccount;
    private Integer expoID;
    private Integer boothID;
    private Integer contentID;
    private Integer contentNumber;
    private LocalDateTime clickAt;
    private String role;
    private Boolean isOwner;
    private Boolean isCollaborator;

    public static ContentLogResponse fromContentLog(ContentLog contentLog){
        ContentLogResponse response = new ContentLogResponse();

        response.setSessionID(contentLog.getSessionID());
        response.setUserAccount(contentLog.getUser().getUserAccount());
        response.setExpoID(contentLog.getExpo().getExpoID());
        response.setBoothID(contentLog.getBooth().getBoothID());
        response.setContentID(contentLog.getContent().getId());
        response.setContentNumber(contentLog.getContentNumber());
        response.setClickAt(contentLog.getClickAt());
        response.setRole(contentLog.getRole());
        response.setIsOwner(contentLog.getIsOwner());
        response.setIsCollaborator(contentLog.getIsCollaborator());

        return response;
    }
}
