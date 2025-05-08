package com.example.api.DTO.Response;

import com.example.api.Entity.OpenMode;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Component
public class BoothEditResponse {
    private String name;
    private String avatar;
    private String introduction;
    private OpenMode openMode;
    private Boolean openStatus;
    private LocalDateTime openStart;
    private LocalDateTime openEnd;
    private Integer maxParticipants;
    private Boolean display;
    private List<UserListResponse> collaborators;
}
