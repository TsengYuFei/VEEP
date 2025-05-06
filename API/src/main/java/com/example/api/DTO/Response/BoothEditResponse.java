package com.example.api.DTO.Response;

import com.example.api.Entity.OpenMode;
import com.example.api.Entity.User;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private List<CollaboratorUserResponse> collaborators;
}
